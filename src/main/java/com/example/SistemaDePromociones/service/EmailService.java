package com.example.SistemaDePromociones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationCode(String to, String code) {
        logger.info("Intentando enviar código de verificación a: {}", to);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Código de verificación FoodIx");
            message.setText("Tu código de verificación es: " + code + "\n\n" +
                          "Este código expirará en 10 minutos.\n\n" +
                          "Si no solicitaste este código, por favor ignora este mensaje.");

            emailSender.send(message);
            logger.info("Código de verificación enviado exitosamente a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar el código de verificación a: " + to, e);
            throw new RuntimeException("No se pudo enviar el correo de verificación: " + e.getMessage());
        }
    }
    
    /**
     * Envía notificación de rechazo al restaurante
     */
    public void sendRestaurantRejectionNotification(String to, String nombreRestaurante, String motivoRechazo) {
        logger.info("Enviando notificación de rechazo a restaurante: {}", to);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Notificación sobre su solicitud de registro - FoodIx");
            
            String emailBody = String.format(
                "Estimado(a) propietario(a) de %s,\n\n" +
                "Lamentamos informarle que su solicitud de registro en la plataforma FoodIx ha sido rechazada.\n\n" +
                "Motivo del rechazo:\n%s\n\n" +
                "Si desea obtener más información o considerar una nueva solicitud, " +
                "por favor contacte con nuestro equipo de soporte.\n\n" +
                "Gracias por su interés en FoodIx.\n\n" +
                "Atentamente,\n" +
                "Equipo de FoodIx\n" +
                "soporte@foodix.com",
                nombreRestaurante,
                motivoRechazo
            );
            
            message.setText(emailBody);
            
            emailSender.send(message);
            logger.info("Notificación de rechazo enviada exitosamente a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar notificación de rechazo a: " + to, e);
            // No lanzar excepción para no detener el proceso de rechazo
            logger.warn("El restaurante fue rechazado pero no se pudo enviar el correo");
        }
    }
}