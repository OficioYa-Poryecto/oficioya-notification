package oficioya_notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import oficioya_notification.model.NotificationType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationTest {

    @Test
    void settersAndGetters() {
        Notification n = new Notification();
        UUID id = UUID.randomUUID();
        n.setId(id);
        n.setTitle("t");
        n.setDescription("d");
        n.setState(NotificationType.INCOMING_REQUEST);
        n.setReceiverId(UUID.randomUUID());
        n.setRequestId(UUID.randomUUID());
        n.setDateCreated(LocalDateTime.now());
        n.setDateSent(LocalDateTime.now());

        assertEquals(id, n.getId());
        assertEquals("t", n.getTitle());
        assertNotNull(n.getDateCreated());
    }
}