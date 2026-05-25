package com.sps.repository;

import com.sps.model.ItemCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {
    List<ItemCompra> findByCompraId(Long compraId);
}
