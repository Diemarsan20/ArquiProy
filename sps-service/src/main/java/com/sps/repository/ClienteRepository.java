package com.sps.repository;

// Basado en ClienteRepository de 05_textos_h2
import com.sps.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCedula(String cedula);
    Cliente findByCorreo(String correo);
}
