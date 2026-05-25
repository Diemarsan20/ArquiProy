package com.sps.messaging;

// Basado en ListenerEventos de 08_textos_eda_f.txt
import com.sps.config.RabbitMQConfig;
import com.sps.dto.PagoNotificacion;
import com.sps.service.CompraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaludPayListener {

    private static final Logger log = LoggerFactory.getLogger(SaludPayListener.class);

    @Autowired
    private CompraService compraService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SALUDPAY_IN)
    public void recibirPago(PagoNotificacion pago) {
        log.info("[SPS-MQ] Pago recibido desde SaludPay — compra: {} — cédula: {} — valor: ${}",
                pago.getNumeroCompra(), pago.getCedulaCliente(), pago.getValorPagado());
        compraService.procesarPagoConfirmado(pago.getNumeroCompra(), pago.getValorPagado());
    }
}
