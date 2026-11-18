package oficioya_notification.mapper;

import org.junit.jupiter.api.Test;

import oficioya_notification.entity.Notification;
import oficioya_notification.model.NotificationModel;
import oficioya_notification.model.NotificationType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationMapperImplTest {

    @Test
    void toModelAndEntity() {
        NotificationMapperImpl mapper = new NotificationMapperImpl();
        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setTitle("t");
        n.setDescription("d");
        n.setState(NotificationType.INCOMING_REQUEST);
        n.setReceiverId(UUID.randomUUID());
        n.setRequestId(UUID.randomUUID());

        NotificationModel model = mapper.toModel(n);
        assertNotNull(model);

        Notification entity = mapper.toEntity(model);
        assertNotNull(entity);
    }
}