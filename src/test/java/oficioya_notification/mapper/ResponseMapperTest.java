package oficioya_notification.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import oficioya_notification.dto.NotificationRequest;
import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.entity.Notification;
import oficioya_notification.model.NotificationType;

class ResponseMapperTest {

    @Test
    void toNotificationDerivesTitleFromMessage() {
        NotificationRequest req = new NotificationRequest(
                UUID.randomUUID(), null, null, "msg", NotificationType.INCOMING_REQUEST,
                UUID.randomUUID(), null, null);
        ResponseMapper mapper = new ResponseMapper();
        Notification n = mapper.toNotification(req);
        assertEquals("msg", n.getTitle());
        assertEquals("msg", n.getDescription());
    }

    @Test
    void toNotificationWithAllFields() {
        NotificationRequest req = new NotificationRequest(
                UUID.randomUUID(), "title", "desc", "msg", NotificationType.INCOMING_REQUEST,
                UUID.randomUUID(), LocalDateTime.now(), "token");
        ResponseMapper mapper = new ResponseMapper();
        Notification n = mapper.toNotification(req);
        assertEquals("title", n.getTitle());
        assertEquals("desc", n.getDescription());
    }

    @Test
    void toResponseMapsFields() {
        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setTitle("t");
        n.setDescription("d");
        n.setState(NotificationType.INCOMING_REQUEST);
        n.setRequestId(UUID.randomUUID());
        n.setDateCreated(LocalDateTime.now());
        n.setDateSent(LocalDateTime.now());

        ResponseMapper mapper = new ResponseMapper();
        NotificationResponse r = mapper.toResponse(n);
        assertNotNull(r);
        assertEquals(n.getId(), r.id());
    }
}