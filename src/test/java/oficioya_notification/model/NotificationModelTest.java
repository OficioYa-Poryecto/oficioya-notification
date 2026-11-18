package oficioya_notification.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationModelTest {

    @Test
    void setters() {
        NotificationModel m = new NotificationModel();
        UUID id = UUID.randomUUID();
        m.setId(id);
        m.setReceiverId(UUID.randomUUID());
        m.setTitle("t");
        m.setDescription("d");
        m.setState(NotificationType.INCOMING_REQUEST);
        m.setRequestId(UUID.randomUUID());
        m.setDateSent(LocalDateTime.now());
        m.setDateCreated(LocalDateTime.now());

        assertEquals(id, m.getId());
        assertEquals("t", m.getTitle());
        assertNotNull(m.getDateCreated());
    }
}