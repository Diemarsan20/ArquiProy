package com.shc.service;

// Basado en patrón de ClienteService de 05_textos_h2
import com.shc.dto.CompraNotificacionSHC;
import com.shc.model.ShcPlan;
import com.shc.model.ShcRegistro;
import com.shc.repository.ShcRegistroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShcService {

    private static final Logger log = LoggerFactory.getLogger(ShcService.class);

    @Autowired
    private ShcRegistroRepository registroRepository;

    public void almacenarCompra(CompraNotificacionSHC notificacion) {
        ShcRegistro registro = new ShcRegistro();
        registro.setCodigoCompra(notificacion.getCodigoCompra());
        registro.setCedulaCliente(notificacion.getCedulaCliente());
        registro.setNombreCliente(notificacion.getNombreCliente());
        registro.setCorreoCliente(notificacion.getCorreoCliente());
        registro.setFechaRecepcion(LocalDateTime.now());

        List<ShcPlan> planes = notificacion.getPlanes().stream().map(pi -> {
            ShcPlan p = new ShcPlan();
            p.setCodigoPlan(pi.getCodigoPlan());
            p.setNombrePlan(pi.getNombrePlan());
            p.setPrecio(pi.getPrecio());
            p.setRegistro(registro);
            return p;
        }).collect(Collectors.toList());

        registro.setPlanes(planes);
        registroRepository.save(registro);

        log.info("[SHC] Registro almacenado — compra: {} — cliente: {}",
                notificacion.getCodigoCompra(), notificacion.getNombreCliente());
    }

    public List<ShcRegistro> obtenerTodos() {
        return registroRepository.findAll();
    }

    public Optional<ShcRegistro> obtenerPorCompra(String codigoCompra) {
        return registroRepository.findByCodigoCompra(codigoCompra);
    }

    public List<ShcRegistro> obtenerPorCliente(String cedula) {
        return registroRepository.findByCedulaCliente(cedula);
    }
}
