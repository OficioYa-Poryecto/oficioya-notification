package oficioya_notification.service;

import oficioya_notification.dto.NotificationRequest;
import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.entity.Notification;
import oficioya_notification.mapper.ResponseMapper;
import oficioya_notification.model.NotificationType;
import oficioya_notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Spy
    private ResponseMapper responseMapper = new ResponseMapper();

    @InjectMocks
    private NotificationService notificationService;

    private Notification sampleNotification;
    private UUID notificationId;
    private UUID receiverId;
    private UUID requestId;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        notificationId = UUID.randomUUID();
        receiverId = UUID.randomUUID();
        requestId = UUID.randomUUID();
        now = LocalDateTime.now();

        sampleNotification = new Notification();
        sampleNotification.setId(notificationId);
        sampleNotification.setReceiverId(receiverId);
        sampleNotification.setRequestId(requestId);
        sampleNotification.setTitle("Nueva solicitud");
        sampleNotification.setDescription("Tienes una reparación pendiente");
        sampleNotification.setState(NotificationType.INCOMING_REQUEST);
        sampleNotification.setDateCreated(now);
        sampleNotification.setDateSent(now);
    }

    @Test
    @DisplayName("Debe listar todas las notificaciones de un usuario")
    void shouldFindAllForUser() {
        when(notificationRepository.findByReceiverId(receiverId)).thenReturn(List.of(sampleNotification));

        List<NotificationResponse> result = notificationService.findAllForUser(receiverId);

        assertEquals(1, result.size());
        assertEquals(notificationId, result.get(0).id());
        assertEquals("Nueva solicitud", result.get(0).title());
        verify(notificationRepository).findByReceiverId(receiverId);
    }

    @Test
    @DisplayName("Debe retornar notificación por ID cuando existe")
    void shouldFindByIdWhenPresent() {
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(sampleNotification));

        Optional<NotificationResponse> result = notificationService.findById(notificationId);

        assertTrue(result.isPresent());
        assertEquals(notificationId, result.get().id());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando no existe el ID")
    void shouldReturnEmptyWhenNotFound() {
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        Optional<NotificationResponse> result = notificationService.findById(notificationId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Debe listar notificaciones por requestId y receiverId")
    void shouldFindAllForARequest() {
        when(notificationRepository.findByRequestIdAndReceiverId(requestId, receiverId))
                .thenReturn(List.of(sampleNotification));

        List<NotificationResponse> result = notificationService.findAllForARequest(requestId, receiverId);

        assertEquals(1, result.size());
        assertEquals(requestId, result.get(0).requestId());
    }

    @Test
    @DisplayName("Debe enviar y guardar una notificación")
    void shouldSendNotification() {
        NotificationRequest request = new NotificationRequest(
                receiverId,
                "Nueva solicitud",
                "Tienes una reparación pendiente",
                NotificationType.INCOMING_REQUEST,
                requestId,
                now
        );

        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> {
            Notification saved = inv.getArgument(0);
            saved.setId(notificationId);
            return saved;
        });

        NotificationResponse response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals(notificationId, response.id());
        assertEquals(now, response.dateSent());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Debe eliminar notificación cuando existe")
    void shouldDeleteNotificationWhenExists() {
        when(notificationRepository.existsById(notificationId)).thenReturn(true);

        boolean deleted = notificationService.deleteNotification(notificationId);

        assertTrue(deleted);
        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    @DisplayName("Debe retornar false al intentar eliminar notificación inexistente")
    void shouldReturnFalseWhenDeletingNonExistent() {
        when(notificationRepository.existsById(notificationId)).thenReturn(false);

        boolean deleted = notificationService.deleteNotification(notificationId);

        assertFalse(deleted);
        verify(notificationRepository, never()).deleteById(any());
    }
}