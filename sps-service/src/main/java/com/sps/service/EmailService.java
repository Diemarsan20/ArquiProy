package com.sps.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String FROM = "sps@javeriana.edu.co";

    @Autowired
    private JavaMailSender mailSender;

    public void notificarAprobacion(String correo, String codigoCompra, Double valorTotal) {
        String asunto = "[SPS] Su compra está lista para pagar";
        String cuerpo = String.format(
            "Estimado cliente,\n\n" +
            "Nos complace informarle que su compra %s ha sido APROBADA por la SNS.\n\n" +
            "Valor a pagar: $%,.0f\n\n" +
            "Ingrese a SaludPay para realizar el pago:\n" +
            "http://localhost:8082\n\n" +
            "Gracias por confiar en SPS.\n",
            codigoCompra, valorTotal
        );
        enviar(correo, asunto, cuerpo);
        log.info("[EMAIL] Notificación de aprobación enviada a {}", correo);
    }

    public void notificarPagoConfirmado(String correo, String codigoCompra, Double valorPagado) {
        String asunto = "[SPS] Su compra ha sido completada";
        String cuerpo = String.format(
            "Estimado cliente,\n\n" +
            "Le confirmamos que su compra %s ha sido COMPLETADA exitosamente.\n\n" +
            "Valor pagado: $%,.0f\n\n" +
            "Sus planes de salud ya están activos. Puede consultar su historia clínica\n" +
            "y sus citas médicas en los sistemas correspondientes.\n\n" +
            "Gracias por confiar en SPS.\n",
            codigoCompra, valorPagado
        );
        enviar(correo, asunto, cuerpo);
        log.info("[EMAIL] Confirmación de pago enviada a {}", correo);
    }

    public void notificarRechazo(String correo, String codigoCompra) {
        String asunto = "[SPS] Su compra fue rechazada por la SNS";
        String cuerpo = String.format(
            "Estimado cliente,\n\n" +
            "Lamentamos informarle que su compra %s fue RECHAZADA por la " +
            "Superintendencia Nacional de Salud (SNS).\n\n" +
            "Uno o más de los planes seleccionados no están autorizados para la venta.\n\n" +
            "Por favor comuníquese con nosotros para más información.\n\n" +
            "SPS - Sistema de Compra de Planes de Salud\n",
            codigoCompra
        );
        enviar(correo, asunto, cuerpo);
        log.info("[EMAIL] Notificación de rechazo enviada a {}", correo);
    }

    private void enviar(String destinatario, String asunto, String cuerpo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(FROM);
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mailSender.send(mensaje);
        } catch (Exception e) {
            log.error("[EMAIL] Error enviando correo a {}: {}", destinatario, e.getMessage());
        }
    }
}
