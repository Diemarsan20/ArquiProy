package com.sps.controller;

// Basado en ProductoController de 05_textos_h2, adaptado para Plan
import com.sps.model.Plan;
import com.sps.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<Plan> listarTodos() {
        return planService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> obtenerPorId(@PathVariable Long id) {
        return planService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Plan crear(@RequestBody Plan plan) {
        return planService.guardar(plan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizar(@PathVariable Long id, @RequestBody Plan planActualizado) {
        return planService.obtenerPorId(id).map(plan -> {
            plan.setNombre(planActualizado.getNombre());
            plan.setDescripcion(planActualizado.getDescripcion());
            plan.setPrecio(planActualizado.getPrecio());
            return ResponseEntity.ok(planService.guardar(plan));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (planService.obtenerPorId(id).isPresent()) {
            planService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public List<Plan> buscarPorNombre(@RequestParam String nombre) {
        return planService.buscarPorNombre(nombre);
    }
}
