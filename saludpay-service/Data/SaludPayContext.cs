// Basado en ClientesContext de textos_net (2).txt, adaptado para Salud Pay
using Microsoft.EntityFrameworkCore;
using SaludPayApi.Models;

namespace SaludPayApi.Data;

public class SaludPayContext : DbContext
{
    public SaludPayContext(DbContextOptions<SaludPayContext> options) : base(options) { }

    public DbSet<CompraPendiente> ComprasPendientes => Set<CompraPendiente>();
}
