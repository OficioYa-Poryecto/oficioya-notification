package oficioya_notification.service;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import oficioya_notification.dto.ReviewReportRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewReportNotificationServiceTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void noAdminEmail() {
        ReviewReportNotificationService service = new ReviewReportNotificationService();
        ReviewReportRequest req = new ReviewReportRequest("r1", "u1", "reason", "content", null);
        String result = service.notifyReviewReported(req);
        assertTrue(result.contains("email deshabilitado"));
    }

    @Test
    void noMailSender() throws Exception {
        ReviewReportNotificationService service = new ReviewReportNotificationService();
        setField(service, "adminEmail", "admin@test.com");
        ReviewReportRequest req = new ReviewReportRequest("r1", "u1", "reason", "content", null);
        String result = service.notifyReviewReported(req);
        assertTrue(result.contains("mail no configurado"));
    }
}