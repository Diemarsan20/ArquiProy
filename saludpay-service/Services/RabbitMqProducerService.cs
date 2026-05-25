// Basado en RabbitMqService de textos_RabbitMQ (2).txt
using RabbitMQ.Client;
using System.Text;
using System.Text.Json;

namespace SaludPayApi.Services;

public class PagoMQ
{
    public string CedulaCliente { get; set; } = string.Empty;
    public string NumeroCompra  { get; set; } = string.Empty;
    public double ValorPagado   { get; set; }
}

public class RabbitMqProducerService
{
    private readonly IConnection _connection;
    private readonly IModel _channel;
    private readonly ILogger<RabbitMqProducerService> _logger;

    public RabbitMqProducerService(IConfiguration config, ILogger<RabbitMqProducerService> logger)
    {
        _logger = logger;
        var factory = new ConnectionFactory
        {
            HostName = config["RabbitMQ:Host"] ?? "localhost",
            Port     = int.Parse(config["RabbitMQ:Port"] ?? "5672"),
            UserName = config["RabbitMQ:Username"] ?? "adminclientes",
            Password = config["RabbitMQ:Password"] ?? "adminclientes123"
        };

        // Retry de conexión — RabbitMQ puede no estar listo en el arranque
        IConnection? conn = null;
        for (int i = 1; i <= 10; i++)
        {
            try { conn = factory.CreateConnection(); break; }
            catch { Thread.Sleep(3000); }
        }
        _connection = conn ?? factory.CreateConnection();
        _channel    = _connection.CreateModel();

        _channel.QueueDeclare(queue:      "saludpay-pagos",
                              durable:    true,
                              exclusive:  false,
                              autoDelete: false,
                              arguments:  null);
    }

    public void EnviarPago(PagoMQ pago)
    {
        // Serializar en camelCase para compatibilidad con Jackson (Spring Boot)
        var options = new JsonSerializerOptions { PropertyNamingPolicy = JsonNamingPolicy.CamelCase };
        string json = JsonSerializer.Serialize(pago, options);
        var body    = Encoding.UTF8.GetBytes(json);

        // Establecer content_type para que Jackson2JsonMessageConverter lo procese
        var props = _channel.CreateBasicProperties();
        props.ContentType = "application/json";

        _channel.BasicPublish(exchange:        "",
                              routingKey:      "saludpay-pagos",
                              basicProperties: props,
                              body:            body);

        _logger.LogInformation("[SaludPay-MQ] Pago enviado a SPS — compra: {compra}", pago.NumeroCompra);
    }
}
