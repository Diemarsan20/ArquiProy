package com.shc.repository;

// Basado en patrón de 05_textos_h2
import com.shc.model.ShcRegistro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShcRegistroRepository extends JpaRepository<ShcRegistro, Long> {
    Optional<ShcRegistro> findByCodigoCompra(String codigoCompra);
    List<ShcRegistro> findByCedulaCliente(String cedulaCliente);
}
