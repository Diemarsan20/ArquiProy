// Basado en el modelo Cliente de textos_net (2).txt, adaptado para Salud Pay
namespace SaludPayApi.Models;

public class CompraPendiente
{
    public int Id { get; set; }
    public string CedulaCliente { get; set; } = string.Empty;
    public string NumeroCompra  { get; set; } = string.Empty;
    public double ValorTotal    { get; set; }
    public bool   Pagada        { get; set; } = false;
}
