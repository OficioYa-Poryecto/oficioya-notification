package oficioya_notification.service;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import oficioya_notification.dto.ReviewReportRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewReportNotificationServiceTest2 {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void mailSenderThrows() throws Exception {
        ReviewReportNotificationService service = new ReviewReportNotificationService();
        setField(service, "adminEmail", "admin@test.com");
        org.springframework.mail.javamail.JavaMailSender mailSender = Mockito.mock(org.springframework.mail.javamail.JavaMailSender.class);
        Mockito.doThrow(new RuntimeException("boom")).when(mailSender).send(Mockito.any(org.springframework.mail.SimpleMailMessage.class));
        setField(service, "mailSender", mailSender);
        ReviewReportRequest req = new ReviewReportRequest("r1", "u1", "reason", "content", null);
        String result = service.notifyReviewReported(req);
        assertTrue(result.contains("error al enviar correo"));
    }
}