package oficioya_notification.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.service.NotificationService;
import oficioya_notification.service.ReviewReportNotificationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NotificationControllerUnitTest {

    @Test
    void getByIdNotFound() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        UUID id = UUID.randomUUID();
        when(svc.findById(id)).thenReturn(Optional.empty());
        var resp = controller.getById(id);
        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    void getAllUserNotifications() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        UUID userId = UUID.randomUUID();
        when(svc.findAllForUser(userId)).thenReturn(List.of());
        var resp = controller.getAllUserNotifications(userId);
        assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void getByIdFound() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        UUID id = UUID.randomUUID();
        NotificationResponse resp = new NotificationResponse(id, "t", "d", oficioya_notification.model.NotificationType.INCOMING_REQUEST, id, null, null);
        when(svc.findById(id)).thenReturn(java.util.Optional.of(resp));
        var r = controller.getById(id);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void getAllUserNotificationsOk() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        UUID userId = UUID.randomUUID();
        when(svc.findAllForUser(userId)).thenReturn(List.of());
        var r = controller.getAllUserNotifications(userId);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void sendNotification() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        oficioya_notification.dto.NotificationRequest req = new oficioya_notification.dto.NotificationRequest(
                java.util.UUID.randomUUID(), "t", "d", "m", oficioya_notification.model.NotificationType.INCOMING_REQUEST,
                java.util.UUID.randomUUID(), null, null);
        oficioya_notification.dto.NotificationResponse resp = new oficioya_notification.dto.NotificationResponse(
                java.util.UUID.randomUUID(), "t", "d", oficioya_notification.model.NotificationType.INCOMING_REQUEST,
                req.requestId(), null, null);
        when(svc.sendNotification(req)).thenReturn(resp);
        var r = controller.send(req);
        assertEquals(201, r.getStatusCode().value());
    }

    @Test
    void deleteNotificationNotFound() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        java.util.UUID id = java.util.UUID.randomUUID();
        when(svc.deleteNotification(id)).thenReturn(false);
        var r = controller.deleteNotification(id);
        assertEquals(404, r.getStatusCode().value());
    }

    @Test
    void deleteNotification() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        UUID id = UUID.randomUUID();
        when(svc.deleteNotification(id)).thenReturn(true);
        var resp = controller.deleteNotification(id);
        assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void getAllUserNotificationsForARequest() {
        NotificationService svc = Mockito.mock(NotificationService.class);
        ReviewReportNotificationService reviewSvc = Mockito.mock(ReviewReportNotificationService.class);
        NotificationController controller = new NotificationController(svc, reviewSvc);
        UUID userId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        when(svc.findAllForARequest(requestId, userId)).thenReturn(List.of());
        var resp = controller.getAllUserNotificationsForARequest(userId, requestId);
        assertEquals(200, resp.getStatusCode().value());
    }
}