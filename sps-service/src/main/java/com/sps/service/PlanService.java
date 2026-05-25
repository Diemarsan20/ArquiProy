package com.sps.service;

// Basado en ProductoService de 05_textos_h2, adaptado para Plan
import com.sps.model.Plan;
import com.sps.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> obtenerTodos() {
        return planRepository.findAll();
    }

    public Optional<Plan> obtenerPorId(Long id) {
        return planRepository.findById(id);
    }

    public Plan guardar(Plan plan) {
        return planRepository.save(plan);
    }

    public void eliminar(Long id) {
        planRepository.deleteById(id);
    }

    public List<Plan> buscarPorNombre(String nombre) {
        return planRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Plan> buscarPorCodigo(String codigo) {
        return planRepository.findByCodigo(codigo);
    }
}
