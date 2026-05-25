package com.shc.messaging;

// Basado en ListenerEventos de 08_textos_eda_f.txt
import com.shc.config.RabbitMQConfig;
import com.shc.dto.CompraNotificacionSHC;
import com.shc.service.ShcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShcListener {

    private static final Logger log = LoggerFactory.getLogger(ShcListener.class);

    @Autowired
    private ShcService shcService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SHC)
    public void recibirCompra(CompraNotificacionSHC notificacion) {
        log.info("[SHC-MQ] Compra recibida: {} — cliente: {}",
                notificacion.getCodigoCompra(), notificacion.getCedulaCliente());
        shcService.almacenarCompra(notificacion);
    }
}
