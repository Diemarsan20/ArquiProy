namespace SaludPayApi.Models;

public class PagoRequest
{
    public string CedulaCliente { get; set; } = string.Empty;
    public string NumeroCompra  { get; set; } = string.Empty;
    public double ValorPagado   { get; set; }
}
