package oficioya_notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import oficioya_notification.dto.EmailRequest;

@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public String sendEmail(EmailRequest request) {
        if (request.to() == null || request.to().isBlank()) {
            log.warn("Destinatario (to) vacío; no se envía correo.");
            return "Recibido: destinatario vacío";
        }
        if (mailSender == null) {
            log.warn("JavaMailSender no configurado; correo no enviado a {}", request.to());
            return "Recibido: mail no configurado (destinatario " + request.to() + ")";
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.to());
            message.setSubject(request.subject() != null ? request.subject() : "Notificación OficioYa");
            message.setText(request.body() != null ? request.body() : "");
            mailSender.send(message);
            log.info("Correo enviado a {} (tipo: {})", request.to(), request.type());
            return "Recibido: correo enviado a " + request.to();
        } catch (Exception e) {
            log.error("Error enviando correo a {}: {}", request.to(), e.getMessage());
            return "Recibido: error al enviar correo (" + e.getMessage() + ")";
        }
    }
}
