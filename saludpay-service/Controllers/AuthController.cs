// Basado en ClientesController de textos_net (2).txt — adaptado para auth JWT
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using SaludPayApi.Models;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace SaludPayApi.Controllers;

[ApiController]
[Route("api/[controller]")]
public class AuthController : ControllerBase
{
    private readonly IConfiguration _config;

    private static readonly Dictionary<string, string> Usuarios = new()
    {
        { "11111111", "pass123" },
        { "22222222", "pass123" },
        { "33333333", "pass123" }
    };

    public AuthController(IConfiguration config)
    {
        _config = config;
    }

    [HttpPost("login")]
    public IActionResult Login([FromBody] LoginRequest req)
    {
        if (!Usuarios.TryGetValue(req.Cedula, out var pwd) || pwd != req.Password)
            return Unauthorized(new { mensaje = "Credenciales inválidas" });

        var token = GenerarToken(req.Cedula);
        return Ok(new { token, cedula = req.Cedula });
    }

    private string GenerarToken(string cedula)
    {
        var secret  = _config["Jwt:Secret"] ?? "SaludPay-ClaveSecretaJaveriana2026-ArquiSoft!";
        var key     = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secret));
        var creds   = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
        var expira  = DateTime.UtcNow.AddHours(24);

        var token = new JwtSecurityToken(
            claims:   new[] { new Claim(ClaimTypes.NameIdentifier, cedula) },
            expires:  expira,
            signingCredentials: creds);

        return new JwtSecurityTokenHandler().WriteToken(token);
    }
}
