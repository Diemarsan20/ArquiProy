package com.sps.service;

import com.sps.dto.CompraNotificacionSAM;
import com.sps.dto.CompraNotificacionSHC;
import com.sps.model.*;
import com.sps.repository.CompraRepository;
import com.sps.repository.ItemCompraRepository;
import com.sps.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private static final Logger log = LoggerFactory.getLogger(CompraService.class);

    @Autowired private CompraRepository    compraRepository;
    @Autowired private ItemCompraRepository itemCompraRepository;
    @Autowired private PlanRepository      planRepository;
    @Autowired private MensajeriaService   mensajeriaService;
    @Autowired private EmailService        emailService;

    public Compra crearCompra(Cliente cliente, List<Long> planIds) {
        Compra compra = new Compra();
        compra.setCodigo("SPS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        compra.setCliente(cliente);
        compra.setEstadoCompra(Compra.ESTADO_PENDIENTE_VALIDACION);
        compra.setFechaCreacion(LocalDateTime.now());
        compra.setFechaActualizacion(LocalDateTime.now());

        List<ItemCompra> items = new ArrayList<>();
        double total = 0.0;

        for (Long planId : planIds) {
            Plan plan = planRepository.findById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + planId));
            ItemCompra item = new ItemCompra();
            item.setCompra(compra);
            item.setPlan(plan);
            item.setPrecio(plan.getPrecio());
            item.setEstadoValidacionSns(ItemCompra.SNS_PENDIENTE);
            items.add(item);
            total += plan.getPrecio();
        }

        compra.setValorTotal(total);
        compra.setItems(items);
        Compra guardada = compraRepository.save(compra);
        itemCompraRepository.saveAll(items);

        log.info("[SPS] Compra creada: {} — cliente: {} — valor: ${} — PENDIENTE_VALIDACION",
                guardada.getCodigo(), cliente.getCorreo(), total);

        return guardada;
    }

    public Optional<Compra> obtenerPorId(Long id) {
        return compraRepository.findById(id);
    }

    public Optional<Compra> obtenerPorCodigo(String codigo) {
        return compraRepository.findByCodigo(codigo);
    }

    public List<Compra> obtenerPorCliente(Long clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }

    // Llamado cuando SaludPay confirma el pago via RabbitMQ → estado PAGADO + email al cliente
    public void procesarPagoConfirmado(String codigoCompra, Double valorPagado) {
        compraRepository.findByCodigo(codigoCompra).ifPresent(compra -> {
            compra.setEstadoCompra(Compra.ESTADO_PAGADO);
            compra.setFechaActualizacion(LocalDateTime.now());
            compraRepository.save(compra);

            log.info("[SPS] Pago recibido para compra {} — valor: ${}. Esperando confirmación del cliente.", codigoCompra, valorPagado);
            emailService.notificarPagoConfirmado(
                    compra.getCliente().getCorreo(), codigoCompra, valorPagado);
        });
    }

    // Llamado cuando el cliente confirma que leyó el aviso → estado TERMINADA + SHC + SAM
    public boolean confirmarRecepcion(String codigoCompra) {
        return compraRepository.findByCodigo(codigoCompra)
                .filter(c -> Compra.ESTADO_PAGADO.equals(c.getEstadoCompra()))
                .map(compra -> {
                    compra.setEstadoCompra(Compra.ESTADO_TERMINADA);
                    compra.setFechaActualizacion(LocalDateTime.now());
                    compraRepository.save(compra);

                    enviarASHC(compra);
                    enviarASAM(compra);

                    log.info("[SPS] Compra {} TERMINADA — cliente confirmó el aviso. Info enviada a SHC y SAM.", compra.getCodigo());
                    return true;
                })
                .orElse(false);
    }

    private void enviarASHC(Compra compra) {
        CompraNotificacionSHC notif = new CompraNotificacionSHC();
        notif.setCodigoCompra(compra.getCodigo());
        notif.setCedulaCliente(compra.getCliente().getCedula());
        notif.setNombreCliente(compra.getCliente().getNombre());
        notif.setCorreoCliente(compra.getCliente().getCorreo());

        List<CompraNotificacionSHC.PlanInfo> planes = compra.getItems().stream()
                .map(item -> new CompraNotificacionSHC.PlanInfo(
                        item.getPlan().getCodigo(),
                        item.getPlan().getNombre(),
                        item.getPrecio()))
                .collect(Collectors.toList());
        notif.setPlanes(planes);
        mensajeriaService.enviarASHC(notif);
    }

    private void enviarASAM(Compra compra) {
        CompraNotificacionSAM notif = new CompraNotificacionSAM();
        notif.setCodigoCompra(compra.getCodigo());
        notif.setCedulaCliente(compra.getCliente().getCedula());

        List<CompraNotificacionSAM.ServicioInfo> servicios = compra.getItems().stream()
                .flatMap(item -> item.getPlan().getServicios().stream()
                        .map(s -> new CompraNotificacionSAM.ServicioInfo(s.getNombre(), s.getTipo(), item.getPlan().getNombre())))
                .collect(Collectors.toList());
        notif.setServicios(servicios);
        mensajeriaService.enviarASAM(notif);
    }
}
