package com.sps.repository;

import com.sps.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByEstadoCompra(String estadoCompra);
    Optional<Compra> findByCodigo(String codigo);
    List<Compra> findByClienteId(Long clienteId);
}
