package com.example.SistemaDePromociones.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("FooDIx - Código de Verificación para Crear tu Cuenta");
            
            String htmlContent = construirHtmlCodigoRegistro(code);
            helper.setText(htmlContent, true);

            emailSender.send(message);
            logger.info("Código de verificación enviado exitosamente a: {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar el código de verificación a: " + to, e);
            throw new RuntimeException("No se pudo enviar el correo de verificación: " + e.getMessage());
        }
    }
    
    /**
<<<<<<< HEAD
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
=======
     * Enviar código de verificación con HTML para recuperación de contraseña
     */
    public void enviarCodigoVerificacion(String destinatario, String codigo) throws MessagingException {
        logger.info("📧 [EMAIL] Preparando envío de código HTML a: {}", destinatario);
        
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("FooDIx - Código de Verificación para Recuperación de Contraseña");
            
            String htmlContent = construirHtmlCodigoVerificacion(codigo);
            helper.setText(htmlContent, true);
            
            emailSender.send(message);
            logger.info("✅ [EMAIL] Código enviado exitosamente a: {}", destinatario);
        } catch (Exception e) {
            logger.error("❌ [EMAIL] Error al enviar código a: " + destinatario, e);
            throw e;
        }
    }
    
    /**
     * Construir HTML del correo de verificación para registro
     */
    private String construirHtmlCodigoRegistro(String codigo) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 40px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden;">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #a0002a 0%%, #FFD700 100%%); color: white; padding: 30px; text-align: center;">
                                        <h1 style="margin: 0; font-size: 28px;">🍔 FooDIx</h1>
                                        <p style="margin: 10px 0 0 0; font-size: 16px;">Verificación de Cuenta</p>
                                    </td>
                                </tr>
                                <!-- Content -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <h2 style="color: #333; margin-top: 0;">¡Bienvenido a FooDIx!</h2>
                                        <p style="color: #666; font-size: 16px; line-height: 1.6;">
                                            Estás a un paso de crear tu cuenta. 
                                            Utiliza el siguiente código de verificación para completar tu registro:
                                        </p>
                                        <!-- Code Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 30px 0;">
                                            <tr>
                                                <td style="background-color: #fff8dc; border: 2px dashed #a0002a; border-radius: 10px; padding: 30px; text-align: center;">
                                                    <span style="font-size: 48px; font-weight: bold; color: #a0002a; letter-spacing: 8px; font-family: 'Courier New', monospace;">""" 
                + codigo + 
                """
</span>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- Warning Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 20px 0;">
                                            <tr>
                                                <td style="background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; border-radius: 4px;">
                                                    <p style="margin: 0 0 10px 0; color: #856404; font-weight: bold;">⚠️ Importante:</p>
                                                    <ul style="margin: 0; padding-left: 20px; color: #856404;">
                                                        <li>Este código es válido por <strong>10 minutos</strong></li>
                                                        <li>No compartas este código con nadie</li>
                                                        <li>Si no solicitaste crear una cuenta, ignora este correo</li>
                                                    </ul>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #6c757d; font-size: 14px; margin-top: 30px;">
                                            Si tienes problemas, contacta a nuestro equipo de soporte.
                                        </p>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 20px; text-align: center;">
                                        <p style="margin: 0 0 5px 0; color: #6c757d; font-size: 14px;">
                                            © 2025 FooDIx - Sistema de Gestión de Restaurantes
                                        </p>
                                        <p style="margin: 0; color: #6c757d; font-size: 14px;">
                                            Este es un correo automático, por favor no respondas a este mensaje.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """;
    }
    
    /**
     * Construir HTML del correo de verificación para recuperación de contraseña
     */
    private String construirHtmlCodigoVerificacion(String codigo) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 40px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden;">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center;">
                                        <h1 style="margin: 0; font-size: 28px;">🍔 FooDIx</h1>
                                        <p style="margin: 10px 0 0 0; font-size: 16px;">Recuperación de Contraseña</p>
                                    </td>
                                </tr>
                                <!-- Content -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <h2 style="color: #333; margin-top: 0;">Código de Verificación</h2>
                                        <p style="color: #666; font-size: 16px; line-height: 1.6;">
                                            Hemos recibido una solicitud para restablecer tu contraseña. 
                                            Utiliza el siguiente código de verificación:
                                        </p>
                                        <!-- Code Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 30px 0;">
                                            <tr>
                                                <td style="background-color: #f8f9ff; border: 2px dashed #667eea; border-radius: 10px; padding: 30px; text-align: center;">
                                                    <span style="font-size: 48px; font-weight: bold; color: #667eea; letter-spacing: 8px; font-family: 'Courier New', monospace;">""" 
                + codigo + 
                """
</span>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- Warning Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 20px 0;">
                                            <tr>
                                                <td style="background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; border-radius: 4px;">
                                                    <p style="margin: 0 0 10px 0; color: #856404; font-weight: bold;">⚠️ Importante:</p>
                                                    <ul style="margin: 0; padding-left: 20px; color: #856404;">
                                                        <li>Este código es válido por <strong>10 minutos</strong></li>
                                                        <li>No compartas este código con nadie</li>
                                                        <li>Si no solicitaste este código, ignora este correo</li>
                                                    </ul>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #6c757d; font-size: 14px; margin-top: 30px;">
                                            Si tienes problemas, contacta a nuestro equipo de soporte.
                                        </p>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 20px; text-align: center;">
                                        <p style="margin: 0 0 5px 0; color: #6c757d; font-size: 14px;">
                                            © 2025 FooDIx - Sistema de Gestión de Restaurantes
                                        </p>
                                        <p style="margin: 0; color: #6c757d; font-size: 14px;">
                                            Este es un correo automático, por favor no respondas a este mensaje.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """;
    }
>>>>>>> 59fbe85265c8c22762bbefd85ee389a0d513f8ab
}