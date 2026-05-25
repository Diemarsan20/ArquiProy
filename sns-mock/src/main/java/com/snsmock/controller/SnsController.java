package com.snsmock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock de la SNS (Superintendencia Nacional de Salud).
 * Simula los estados: ENPROCESO (primeras 2 consultas) → APROBADO.
 * Planes cuyo código termina en "-R" son RECHAZADOS.
 */
@RestController
@RequestMapping("/api/sns")
public class SnsController {

    private static final Logger log = LoggerFactory.getLogger(SnsController.class);

    private final ConcurrentHashMap<String, AtomicInteger> conteoConsultas = new ConcurrentHashMap<>();

    @GetMapping("/validar")
    public Map<String, String> validarPlan(@RequestParam String codigoPlan) {
        AtomicInteger conteo = conteoConsultas.computeIfAbsent(codigoPlan, k -> new AtomicInteger(0));
        int veces = conteo.incrementAndGet();

        String estado;
        if (codigoPlan.toUpperCase().endsWith("-R")) {
            estado = "RECHAZADO";
        } else if (veces <= 2) {
            estado = "ENPROCESO";
        } else {
            estado = "APROBADO";
        }

        log.info("[SNS-Mock] Plan {} — consulta #{} → {}", codigoPlan, veces, estado);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("codigoPlan", codigoPlan);
        respuesta.put("estado", estado);
        return respuesta;
    }

    @DeleteMapping("/reset/{codigoPlan}")
    public Map<String, String> resetConteo(@PathVariable String codigoPlan) {
        conteoConsultas.remove(codigoPlan);
        return Map.of("mensaje", "Contador reseteado para " + codigoPlan);
    }
}
