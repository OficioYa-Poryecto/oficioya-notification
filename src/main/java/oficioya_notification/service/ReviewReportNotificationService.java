package oficioya_notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import oficioya_notification.dto.ReviewReportRequest;

@Service
public class ReviewReportNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ReviewReportNotificationService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${notification.admin.email:}")
    private String adminEmail;

    public String notifyReviewReported(ReviewReportRequest request) {
        String subject = "Reseña reportada en OficioYa";
        String body = buildBody(request);

        if (adminEmail == null || adminEmail.isBlank()) {
            log.warn("Admin email not configured; skipping email send. Review: {}", request.reviewId());
            return "Recibido: notificación de reseña reportada (email deshabilitado)";
        }

        if (mailSender == null) {
            log.warn("JavaMailSender not available; skipping email send. Review: {}", request.reviewId());
            return "Recibido: notificación de reseña reportada (mail no configurado)";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to admin for reported review {}", request.reviewId());
            return "Recibido: correo enviado al administrador";
        } catch (Exception e) {
            log.error("Failed to send review report email: {}", e.getMessage());
            return "Recibido: error al enviar correo (" + e.getMessage() + ")";
        }
    }

    private String buildBody(ReviewReportRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Se ha reportado una reseña en la plataforma OficioYa.\n\n");
        sb.append("ID de reseña: ").append(request.reviewId()).append("\n");
        sb.append("Reportado por: ").append(request.reportedBy()).append("\n");
        sb.append("Motivo: ").append(request.reason()).append("\n");
        sb.append("Contenido de la reseña: ").append(request.reviewContent()).append("\n");
        if (request.reportedAt() != null) {
            sb.append("Fecha del reporte: ").append(request.reportedAt()).append("\n");
        }
        sb.append("\nPor favor revisa el caso en el panel de administración.");
        return sb.toString();
    }
}
