package com.sps.config;

// Tomado de 08_textos_eda_f.txt y adaptado para SPS
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_SHC            = "shc-queue";
    public static final String QUEUE_SAM            = "sam-queue";
    public static final String QUEUE_SALUDPAY_OUT   = "saludpay-solicitudes";
    public static final String QUEUE_SALUDPAY_IN    = "saludpay-pagos";

    @Bean
    public Queue colaSHC() {
        return new Queue(QUEUE_SHC, true);
    }

    @Bean
    public Queue colaSAM() {
        return new Queue(QUEUE_SAM, true);
    }

    @Bean
    public Queue colaSaludPaySolicitudes() {
        return new Queue(QUEUE_SALUDPAY_OUT, true);
    }

    @Bean
    public Queue colaSaludPayPagos() {
        return new Queue(QUEUE_SALUDPAY_IN, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // Permite recibir mensajes de publicadores no-Spring (ej. .NET) sin header __TypeId__
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
