package com.sps.repository;

// Basado en ProductoRepository de 05_textos_h2
import com.sps.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByCodigo(String codigo);
    List<Plan> findByNombreContainingIgnoreCase(String nombre);
}
