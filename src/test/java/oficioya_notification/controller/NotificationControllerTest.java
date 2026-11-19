package oficioya_notification.controller;

import oficioya_notification.dto.NotificationRequest;
import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.model.NotificationType;
import oficioya_notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private UUID notificationId;
    private UUID userId;
    private UUID requestId;
    private NotificationResponse sampleResponse;

    @BeforeEach
    void setUp() {
        notificationId = UUID.randomUUID();
        userId = UUID.randomUUID();
        requestId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        sampleResponse = new NotificationResponse(
                notificationId,
                "Solicitud aceptada",
                "Un trabajador aceptó tu solicitud",
                NotificationType.ACCEPTED_REQUEST,
                requestId,
                now,
                now
        );
    }

    @Test
    @DisplayName("POST /api/v1/notification debe retornar 201 CREATED")
    void shouldSendNotification() {
        NotificationRequest req = new NotificationRequest(
                userId, "Solicitud aceptada", "Un trabajador aceptó tu solicitud", "msg",
                NotificationType.ACCEPTED_REQUEST, requestId, LocalDateTime.now(), null
        );
        when(notificationService.sendNotification(req)).thenReturn(sampleResponse);

        ResponseEntity<NotificationResponse> response = notificationController.send(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
    }

    @Test
    @DisplayName("GET /api/v1/notification/{id} debe retornar 200 OK cuando existe")
    void shouldGetByIdWhenFound() {
        when(notificationService.findById(notificationId)).thenReturn(Optional.of(sampleResponse));

        ResponseEntity<NotificationResponse> response = notificationController.getById(notificationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
    }

    @Test
    @DisplayName("GET /api/v1/notification/user/{id} debe retornar notificaciones del usuario")
    void shouldGetAllUserNotifications() {
        when(notificationService.findAllForUser(userId)).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<NotificationResponse>> response = notificationController.getAllUserNotifications(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /api/v1/notification/user/{userId}/request/{requestId} debe retornar notificaciones de la solicitud")
    void shouldGetAllUserNotificationsForRequest() {
        when(notificationService.findAllForARequest(requestId, userId)).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<NotificationResponse>> response =
                notificationController.getAllUserNotificationsForARequest(userId, requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("DELETE /api/v1/notification/{id} debe retornar 200 OK si se elimina")
    void shouldDeleteNotificationWhenFound() {
        when(notificationService.deleteNotification(notificationId)).thenReturn(true);

        ResponseEntity<String> response = notificationController.deleteNotification(notificationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notification deleted", response.getBody());
    }

    @Test
    @DisplayName("DELETE /api/v1/notification/{id} debe retornar 404 NOT_FOUND si no existe")
    void shouldReturnNotFoundWhenDeletingMissing() {
        when(notificationService.deleteNotification(notificationId)).thenReturn(false);

        ResponseEntity<String> response = notificationController.deleteNotification(notificationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Notification not found", response.getBody());
    }
}