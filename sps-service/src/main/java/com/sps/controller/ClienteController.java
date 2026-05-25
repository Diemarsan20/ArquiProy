package com.sps.controller;

// Basado en ClienteController de 05_textos_h2
import com.sps.model.Cliente;
import com.sps.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        cliente.setFechaRegistro(LocalDateTime.now());
        return clienteService.guardar(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
        return clienteService.obtenerPorId(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setCorreo(clienteActualizado.getCorreo());
            return ResponseEntity.ok(clienteService.guardar(cliente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (clienteService.obtenerPorId(id).isPresent()) {
            clienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
