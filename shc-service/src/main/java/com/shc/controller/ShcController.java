package com.shc.controller;

// Basado en patrón de ClienteController de 05_textos_h2
import com.shc.model.ShcRegistro;
import com.shc.service.ShcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shc")
public class ShcController {

    @Autowired
    private ShcService shcService;

    @GetMapping("/registros")
    public List<ShcRegistro> listarTodos() {
        return shcService.obtenerTodos();
    }

    @GetMapping("/registros/compra/{codigo}")
    public ResponseEntity<ShcRegistro> obtenerPorCompra(@PathVariable String codigo) {
        return shcService.obtenerPorCompra(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/registros/cliente/{cedula}")
    public List<ShcRegistro> obtenerPorCliente(@PathVariable String cedula) {
        return shcService.obtenerPorCliente(cedula);
    }
}
