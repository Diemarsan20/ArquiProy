// Basado en el consumer de textos_RabbitMQ (2).txt, adaptado como BackgroundService para ASP.NET Core
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System.Text;
using System.Text.Json;
using Microsoft.EntityFrameworkCore;
using SaludPayApi.Data;
using SaludPayApi.Models;

namespace SaludPayApi.Services;

public class SolicitudMQ
{
    public string CedulaCliente { get; set; } = string.Empty;
    public string NumeroCompra  { get; set; } = string.Empty;
    public double ValorTotal    { get; set; }
}

public class RabbitMqConsumerService : BackgroundService
{
    private readonly ILogger<RabbitMqConsumerService> _logger;
    private readonly IConfiguration _config;
    private readonly IServiceScopeFactory _scopeFactory;
    private IConnection? _connection;
    private IModel? _channel;

    public RabbitMqConsumerService(ILogger<RabbitMqConsumerService> logger,
                                   IConfiguration config,
                                   IServiceScopeFactory scopeFactory)
    {
        _logger      = logger;
        _config      = config;
        _scopeFactory = scopeFactory;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        stoppingToken.ThrowIfCancellationRequested();

        var factory = new ConnectionFactory
        {
            HostName = _config["RabbitMQ:Host"] ?? "localhost",
            Port     = int.Parse(_config["RabbitMQ:Port"] ?? "5672"),
            UserName = _config["RabbitMQ:Username"] ?? "adminclientes",
            Password = _config["RabbitMQ:Password"] ?? "adminclientes123"
        };

        // Retry hasta que RabbitMQ esté disponible
        for (int intento = 1; intento <= 10; intento++)
        {
            try
            {
                _connection = factory.CreateConnection();
                _logger.LogInformation("[SaludPay] Conexión a RabbitMQ establecida.");
                break;
            }
            catch (Exception ex)
            {
                _logger.LogWarning("[SaludPay] RabbitMQ no disponible (intento {n}/10): {msg}. Reintentando en 5s...", intento, ex.Message);
                await Task.Delay(5000, stoppingToken);
            }
        }

        if (_connection == null)
        {
            _logger.LogError("[SaludPay] No se pudo conectar a RabbitMQ después de 10 intentos.");
            return;
        }
        _channel    = _connection.CreateModel();

        _channel.QueueDeclare(queue:      "saludpay-solicitudes",
                              durable:    true,
                              exclusive:  false,
                              autoDelete: false,
                              arguments:  null);

        var consumer = new EventingBasicConsumer(_channel);
        consumer.Received += (model, ea) =>
        {
            var body    = ea.Body.ToArray();
            var json    = Encoding.UTF8.GetString(body);
            _logger.LogInformation("[SaludPay-MQ] Solicitud recibida: {json}", json);

            try
            {
                var solicitud = JsonSerializer.Deserialize<SolicitudMQ>(json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

                if (solicitud != null)
                {
                    using var scope   = _scopeFactory.CreateScope();
                    var context       = scope.ServiceProvider.GetRequiredService<SaludPayContext>();
                    var existe        = context.ComprasPendientes
                                              .Any(c => c.NumeroCompra == solicitud.NumeroCompra);
                    if (!existe)
                    {
                        context.ComprasPendientes.Add(new CompraPendiente
                        {
                            CedulaCliente = solicitud.CedulaCliente,
                            NumeroCompra  = solicitud.NumeroCompra,
                            ValorTotal    = solicitud.ValorTotal,
                            Pagada        = false
                        });
                        context.SaveChanges();
                        _logger.LogInformation("[SaludPay] Compra pendiente registrada: {compra}", solicitud.NumeroCompra);
                    }
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "[SaludPay-MQ] Error procesando mensaje");
            }
        };

        _channel.BasicConsume(queue:    "saludpay-solicitudes",
                              autoAck: true,
                              consumer: consumer);

    }

    public override void Dispose()
    {
        _channel?.Close();
        _connection?.Close();
        base.Dispose();
    }
}
