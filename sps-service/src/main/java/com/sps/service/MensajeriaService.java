package com.sps.service;

// Basado en ProductorDeMensajesService de 08_textos_eda_f.txt
import com.sps.config.RabbitMQConfig;
import com.sps.dto.CompraNotificacionSAM;
import com.sps.dto.CompraNotificacionSHC;
import com.sps.dto.SaludPaySolicitud;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensajeriaService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MensajeriaService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarASHC(CompraNotificacionSHC notificacion) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SHC, notificacion);
    }

    public void enviarASAM(CompraNotificacionSAM notificacion) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SAM, notificacion);
    }

    public void enviarASaludPay(SaludPaySolicitud solicitud) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SALUDPAY_OUT, solicitud);
    }
}
