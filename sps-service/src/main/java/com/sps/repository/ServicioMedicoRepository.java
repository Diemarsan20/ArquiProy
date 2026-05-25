package com.sps.repository;

import com.sps.model.ServicioMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioMedicoRepository extends JpaRepository<ServicioMedico, Long> {
    List<ServicioMedico> findByPlanId(Long planId);
}
