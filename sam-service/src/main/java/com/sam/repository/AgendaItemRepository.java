package com.sam.repository;

// Basado en patrón de 05_textos_h2
import com.sam.model.AgendaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaItemRepository extends JpaRepository<AgendaItem, Long> {
    List<AgendaItem> findByCedulaCliente(String cedulaCliente);
    List<AgendaItem> findByCodigoCompra(String codigoCompra);
    List<AgendaItem> findByTipoServicio(String tipoServicio);
}
