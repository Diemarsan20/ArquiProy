// Basado en Program.cs de textos_net (2).txt — adaptado para SaludPay
using Microsoft.EntityFrameworkCore;
using SaludPayApi.Data;
using SaludPayApi.Services;

var builder = WebApplication.CreateBuilder(args);

// InMemory DB (equivalente funcional a H2 en Spring Boot para este servicio .NET)
builder.Services.AddDbContext<SaludPayContext>(opt =>
    opt.UseInMemoryDatabase("SaludPayDB"));

// RabbitMQ consumer como BackgroundService — basado en textos_RabbitMQ (2).txt
builder.Services.AddHostedService<RabbitMqConsumerService>();

// RabbitMQ producer — basado en textos_RabbitMQ (2).txt
builder.Services.AddSingleton<RabbitMqProducerService>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

if (app.Environment.IsDevelopment() || app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapControllers();
app.Run();
