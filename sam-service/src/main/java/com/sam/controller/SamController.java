package com.sam.controller;

// Basado en patrón de ProductoController de 05_textos_h2
import com.sam.model.AgendaItem;
import com.sam.service.SamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sam")
public class SamController {

    @Autowired
    private SamService samService;

    @GetMapping("/agenda")
    public List<AgendaItem> listarTodos() {
        return samService.obtenerTodos();
    }

    @GetMapping("/agenda/cliente/{cedula}")
    public List<AgendaItem> obtenerPorCliente(@PathVariable String cedula) {
        return samService.obtenerPorCliente(cedula);
    }

    @GetMapping("/agenda/tipo/{tipo}")
    public List<AgendaItem> obtenerPorTipo(@PathVariable String tipo) {
        return samService.obtenerPorTipo(tipo);
    }
}
