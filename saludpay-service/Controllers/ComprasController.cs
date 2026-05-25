// Basado en ClientesController de textos_net (2).txt
using Microsoft.AspNetCore.Mvc;
using SaludPayApi.Data;
using SaludPayApi.Models;
using SaludPayApi.Services;

namespace SaludPayApi.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ComprasController : ControllerBase
{
    private readonly SaludPayContext        _context;
    private readonly RabbitMqProducerService _mq;
    private readonly ILogger<ComprasController> _logger;

    public ComprasController(SaludPayContext context,
                             RabbitMqProducerService mq,
                             ILogger<ComprasController> logger)
    {
        _context = context;
        _mq      = mq;
        _logger  = logger;
    }

    [HttpGet("pendientes/{cedula}")]
    public IActionResult ObtenerPendientes(string cedula)
    {
        var pendientes = _context.ComprasPendientes
                                 .Where(c => c.CedulaCliente == cedula && !c.Pagada)
                                 .ToList();
        return Ok(pendientes);
    }

    [HttpGet]
    public IActionResult ListarTodas()
    {
        return Ok(_context.ComprasPendientes.ToList());
    }

    [HttpPost("pagar")]
    public IActionResult ProcesarPago([FromBody] PagoRequest req)
    {
        var compra = _context.ComprasPendientes
                             .FirstOrDefault(c => c.NumeroCompra == req.NumeroCompra
                                               && c.CedulaCliente == req.CedulaCliente
                                               && !c.Pagada);

        if (compra == null)
            return NotFound(new { mensaje = "Compra pendiente no encontrada" });

        compra.Pagada = true;
        _context.SaveChanges();

        _mq.EnviarPago(new PagoMQ
        {
            CedulaCliente = req.CedulaCliente,
            NumeroCompra  = req.NumeroCompra,
            ValorPagado   = req.ValorPagado
        });

        _logger.LogInformation("[SaludPay] Pago procesado para compra {compra} — cedula {cedula}",
            req.NumeroCompra, req.CedulaCliente);

        return Ok(new { mensaje = $"Pago procesado para compra {req.NumeroCompra}", valorPagado = req.ValorPagado });
    }
}
