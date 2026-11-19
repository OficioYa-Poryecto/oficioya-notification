package oficioya_notification.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import oficioya_notification.dto.EmailRequest;
import oficioya_notification.dto.NotificationRequest;
import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.dto.ReviewReportRequest;
import oficioya_notification.service.EmailNotificationService;
import oficioya_notification.service.NotificationService;
import oficioya_notification.service.ReviewReportNotificationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NotificationControllerUnitTest {

    @Test
    void getByIdNotFound() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        UUID id = UUID.randomUUID();
        when(svc.findById(id)).thenReturn(Optional.empty());
        var resp = controller.getById(id);
        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    void getByIdFound() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        UUID id = UUID.randomUUID();
        NotificationResponse resp = new NotificationResponse(id, "t", "d", oficioya_notification.model.NotificationType.INCOMING_REQUEST, id, null, null);
        when(svc.findById(id)).thenReturn(Optional.of(resp));
        var r = controller.getById(id);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void getAllUserNotificationsOk() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        UUID userId = UUID.randomUUID();
        when(svc.findAllForUser(userId)).thenReturn(List.of());
        var r = controller.getAllUserNotifications(userId);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void getAllUserNotificationsForARequest() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        UUID userId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        when(svc.findAllForARequest(requestId, userId)).thenReturn(List.of());
        var r = controller.getAllUserNotificationsForARequest(userId, requestId);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void sendNotificationOk() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        NotificationRequest req = new NotificationRequest(
                UUID.randomUUID(), "t", "d", "m", oficioya_notification.model.NotificationType.INCOMING_REQUEST,
                UUID.randomUUID(), null, null);
        NotificationResponse resp = new NotificationResponse(
                UUID.randomUUID(), "t", "d", oficioya_notification.model.NotificationType.INCOMING_REQUEST,
                req.requestId(), null, null);
        when(svc.sendNotification(req)).thenReturn(resp);
        var r = controller.send(req);
        assertEquals(201, r.getStatusCode().value());
    }

    @Test
    void deleteNotification() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        UUID id = UUID.randomUUID();
        when(svc.deleteNotification(id)).thenReturn(true);
        var r = controller.deleteNotification(id);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void deleteNotificationNotFound() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        UUID id = UUID.randomUUID();
        when(svc.deleteNotification(id)).thenReturn(false);
        var r = controller.deleteNotification(id);
        assertEquals(404, r.getStatusCode().value());
    }

    @Test
    void notifyReviewReportedOk() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        ReviewReportRequest req = Mockito.mock(ReviewReportRequest.class);
        when(reviewSvc.notifyReviewReported(req)).thenReturn("Reported");
        var r = controller.notifyReviewReported(req);
        assertEquals(200, r.getStatusCode().value());
        assertEquals("Reported", r.getBody());
    }

    @Test
    void sendEmailOk() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        EmailNotificationService emailSvc = Mockito.mock(EmailNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc, emailSvc);
        EmailRequest req = Mockito.mock(EmailRequest.class);
        when(emailSvc.sendEmail(req)).thenReturn("Sent");
        var r = controller.sendEmail(req);
        assertEquals(200, r.getStatusCode().value());
        assertEquals("Sent", r.getBody());
    }
}