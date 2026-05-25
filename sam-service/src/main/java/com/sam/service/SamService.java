package com.sam.service;

// Basado en patrón de ClienteService de 05_textos_h2
import com.sam.dto.CompraNotificacionSAM;
import com.sam.model.AgendaItem;
import com.sam.repository.AgendaItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SamService {

    private static final Logger log = LoggerFactory.getLogger(SamService.class);

    @Autowired
    private AgendaItemRepository agendaItemRepository;

    public void almacenarServicios(CompraNotificacionSAM notificacion) {
        List<AgendaItem> items = notificacion.getServicios().stream().map(s -> {
            AgendaItem item = new AgendaItem();
            item.setCodigoCompra(notificacion.getCodigoCompra());
            item.setCedulaCliente(notificacion.getCedulaCliente());
            item.setNombreServicio(s.getNombreServicio());
            item.setTipoServicio(s.getTipo());
            item.setNombrePlan(s.getNombrePlan());
            item.setFechaRecepcion(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());

        agendaItemRepository.saveAll(items);

        log.info("[SAM] {} servicio(s) agendados para compra {} — cliente: {}",
                items.size(), notificacion.getCodigoCompra(), notificacion.getCedulaCliente());
    }

    public List<AgendaItem> obtenerTodos() {
        return agendaItemRepository.findAll();
    }

    public List<AgendaItem> obtenerPorCliente(String cedula) {
        return agendaItemRepository.findByCedulaCliente(cedula);
    }

    public List<AgendaItem> obtenerPorTipo(String tipo) {
        return agendaItemRepository.findByTipoServicio(tipo);
    }
}
