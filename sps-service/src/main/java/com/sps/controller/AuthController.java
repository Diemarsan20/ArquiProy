package com.sps.controller;

import com.sps.dto.LoginRequest;
import com.sps.dto.LoginResponse;
import com.sps.model.Cliente;
import com.sps.service.ClienteService;
import com.sps.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private ClienteService clienteService;
    @Autowired private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<Cliente> cliente = clienteService.autenticar(req.getCedula(), req.getPassword());
        if (cliente.isEmpty()) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        String token = jwtService.generarToken(cliente.get().getCedula());
        return ResponseEntity.ok(new LoginResponse(token, cliente.get().getCedula(), cliente.get().getNombre()));
    }
}
