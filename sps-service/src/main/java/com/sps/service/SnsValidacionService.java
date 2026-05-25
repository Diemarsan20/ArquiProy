package com.sps.service;

// Basado en SincronizadorService de 07-textos.txt — adaptado para validar planes con SNS
import com.sps.dto.SaludPaySolicitud;
import com.sps.dto.SnsRespuesta;
import com.sps.model.Compra;
import com.sps.model.ItemCompra;
import com.sps.repository.CompraRepository;
import com.sps.repository.ItemCompraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class SnsValidacionService {

    private static final Logger log = LoggerFactory.getLogger(SnsValidacionService.class);

    private final WebClient snsWebClient;
    private final CompraRepository compraRepository;
    private final ItemCompraRepository itemCompraRepository;
    private final MensajeriaService mensajeriaService;
    private final EmailService emailService;

    @Autowired
    public SnsValidacionService(WebClient.Builder webClientBuilder,
                                @Value("${sns.base-url}") String snsBaseUrl,
                                CompraRepository compraRepository,
                                ItemCompraRepository itemCompraRepository,
                                MensajeriaService mensajeriaService,
                                EmailService emailService) {
        this.snsWebClient = webClientBuilder.baseUrl(snsBaseUrl).build();
        this.compraRepository = compraRepository;
        this.itemCompraRepository = itemCompraRepository;
        this.mensajeriaService = mensajeriaService;
        this.emailService = emailService;
    }

    @Scheduled(fixedDelay = 15000)
    public void validarComprasPendientes() {
        List<Compra> pendientes = compraRepository.findByEstadoCompra(Compra.ESTADO_PENDIENTE_VALIDACION);
        if (!pendientes.isEmpty()) {
            log.info("[SNS-Scheduler] Validando {} compra(s) pendiente(s)...", pendientes.size());
        }
        for (Compra compra : pendientes) {
            procesarValidacion(compra);
        }
    }

    private void procesarValidacion(Compra compra) {
        boolean alguno_rechazado = false;
        boolean todos_aprobados  = true;

        for (ItemCompra item : compra.getItems()) {
            if (ItemCompra.SNS_APROBADO.equals(item.getEstadoValidacionSns())) continue;
            if (ItemCompra.SNS_RECHAZADO.equals(item.getEstadoValidacionSns())) {
                alguno_rechazado = true;
                todos_aprobados  = false;
                continue;
            }

            String estadoSns = consultarSns(item.getPlan().getCodigo());
            item.setEstadoValidacionSns(estadoSns);
            itemCompraRepository.save(item);
            log.info("[SNS] Compra {} — Plan {} → {}", compra.getCodigo(), item.getPlan().getCodigo(), estadoSns);

            if (ItemCompra.SNS_RECHAZADO.equals(estadoSns)) {
                alguno_rechazado = true;
                todos_aprobados  = false;
            } else if (!ItemCompra.SNS_APROBADO.equals(estadoSns)) {
                todos_aprobados = false;
            }
        }

        if (alguno_rechazado) {
            actualizarEstado(compra, Compra.ESTADO_RECHAZADO);
            log.warn("[SPS] Compra {} RECHAZADA por SNS", compra.getCodigo());
            emailService.notificarRechazo(compra.getCliente().getCorreo(), compra.getCodigo());
        } else if (todos_aprobados) {
            actualizarEstado(compra, Compra.ESTADO_PENDIENTE_PAGO);
            log.info("[SPS] Compra {} APROBADA — enviando a SaludPay", compra.getCodigo());
            emailService.notificarAprobacion(
                compra.getCliente().getCorreo(),
                compra.getCodigo(),
                compra.getValorTotal()
            );
            mensajeriaService.enviarASaludPay(
                new SaludPaySolicitud(
                    compra.getCliente().getCedula(),
                    compra.getCodigo(),
                    compra.getValorTotal()
                )
            );
        }
    }

    private String consultarSns(String codigoPlan) {
        try {
            SnsRespuesta respuesta = snsWebClient.get()
                    .uri("/api/sns/validar?codigoPlan={plan}", codigoPlan)
                    .retrieve()
                    .bodyToMono(SnsRespuesta.class)
                    .timeout(Duration.ofSeconds(3))
                    .onErrorResume(e -> {
                        if (e instanceof TimeoutException) {
                            log.warn("[SNS] Timeout validando plan {}", codigoPlan);
                        } else if (e instanceof WebClientResponseException) {
                            log.warn("[SNS] Error HTTP: {}", ((WebClientResponseException) e).getStatusCode());
                        } else {
                            log.warn("[SNS] Error: {}", e.getMessage());
                        }
                        SnsRespuesta fallback = new SnsRespuesta();
                        fallback.setCodigoPlan(codigoPlan);
                        fallback.setEstado(ItemCompra.SNS_ENPROCESO);
                        return reactor.core.publisher.Mono.just(fallback);
                    })
                    .block();
            return respuesta != null ? respuesta.getEstado() : ItemCompra.SNS_ENPROCESO;
        } catch (Exception e) {
            log.error("[SNS] Excepción inesperada: {}", e.getMessage());
            return ItemCompra.SNS_ENPROCESO;
        }
    }

    private void actualizarEstado(Compra compra, String nuevoEstado) {
        compra.setEstadoCompra(nuevoEstado);
        compra.setFechaActualizacion(LocalDateTime.now());
        compraRepository.save(compra);
    }
}
