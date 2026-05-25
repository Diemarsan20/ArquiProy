package com.sam.messaging;

// Basado en ListenerEventos de 08_textos_eda_f.txt
import com.sam.config.RabbitMQConfig;
import com.sam.dto.CompraNotificacionSAM;
import com.sam.service.SamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SamListener {

    private static final Logger log = LoggerFactory.getLogger(SamListener.class);

    @Autowired
    private SamService samService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SAM)
    public void recibirServicios(CompraNotificacionSAM notificacion) {
        log.info("[SAM-MQ] Servicios recibidos para compra: {} — cliente: {}",
                notificacion.getCodigoCompra(), notificacion.getCedulaCliente());
        samService.almacenarServicios(notificacion);
    }
}
