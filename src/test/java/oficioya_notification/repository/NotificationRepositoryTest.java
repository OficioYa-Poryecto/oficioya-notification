package oficioya_notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import oficioya_notification.TestcontainersConfiguration;
import oficioya_notification.entity.Notification;
import oficioya_notification.mapper.NotificationMapper;
import oficioya_notification.model.NotificationModel;
import oficioya_notification.model.NotificationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository nRepo;

    private final NotificationMapper nMap = Mappers.getMapper(NotificationMapper.class);

    private Notification n1;

    @BeforeEach
    void setup() {
        nRepo.deleteAll();
        UUID n1RecId = UUID.randomUUID();
        UUID n1ReqId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime sent = LocalDateTime.now().plusSeconds(2);
        n1 = new Notification();
        n1.setReceiverId(n1RecId);
        n1.setRequestId(n1ReqId);
        n1.setDateCreated(created);
        n1.setDateSent(sent);
        n1.setState(NotificationType.INCOMING_REQUEST);
        n1.setTitle("Solicitu Entrante");
        n1.setDescription("Nueva reparación cerca de tí");
    }

    @Test
    @DisplayName("Salvar notification básica")
    void shouldSaveBasicNotification() {
        Notification saved = nRepo.save(n1);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(n1.getTitle(), saved.getTitle());
        assertEquals(n1.getDescription(), saved.getDescription());
        assertEquals(n1.getReceiverId(), saved.getReceiverId());
        assertEquals(n1.getRequestId(), saved.getRequestId());
    }

    @Test
    @DisplayName("Salvar dos notificaciones con mismo recipiente")
    void shouldSaveTwoNotificationsToSameReceiver() {
        UUID n1RecId = n1.getReceiverId();
        UUID n1ReqId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime sent = LocalDateTime.now().plusSeconds(2);
        Notification n2 = new Notification();
        n2.setReceiverId(n1RecId);
        n2.setRequestId(n1ReqId);
        n2.setDateCreated(created);
        n2.setDateSent(sent);
        n2.setState(NotificationType.INCOMING_REQUEST);
        n2.setTitle("Solicitu Entrante");
        n2.setDescription("Nueva reparación cerca de tí");

        Notification saved = nRepo.save(n1);
        Notification saved2 = nRepo.save(n2);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(n1.getTitle(), saved.getTitle());
        assertEquals(n1.getDescription(), saved.getDescription());
        assertEquals(n1.getReceiverId(), saved.getReceiverId());
        assertEquals(n1.getRequestId(), saved.getRequestId());

        List<Notification> ls = nRepo.findByReceiverId(n1RecId);

        assertEquals(2, ls.size());
        assertTrue(ls.contains(saved));
        assertTrue(ls.contains(saved2));
        assertEquals(ls.get(0).getReceiverId(), n1.getReceiverId());
        assertEquals(ls.get(1).getReceiverId(), n1.getReceiverId());
    }

    @Test
    @DisplayName("Salvar dos notificaciones de distinto tipo")
    void shouldSaveTwoNotificationsDifferenteType() {
        UUID n1RecId = n1.getReceiverId();
        UUID n1ReqId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime sent = LocalDateTime.now().plusSeconds(2);
        Notification n2 = new Notification();
        n2.setReceiverId(n1RecId);
        n2.setRequestId(n1ReqId);
        n2.setDateCreated(created);
        n2.setDateSent(sent);
        n2.setState(NotificationType.ACCEPTED_REQUEST);
        n2.setTitle("Solicitu Aceptada");
        n2.setDescription("Carlos ramírez atenderá tu solicitud");

        Notification saved = nRepo.save(n1);
        Notification saved2 = nRepo.save(n2);

        List<Notification> ls = nRepo.findByReceiverId(n1RecId);
        assertTrue(ls.contains(saved));
        assertTrue(ls.contains(saved2));

        assertEquals(NotificationType.INCOMING_REQUEST, ls.get(0).getState());
        assertEquals(NotificationType.ACCEPTED_REQUEST, ls.get(1).getState());

        assertEquals(created, ls.get(1).getDateCreated());
        assertEquals(sent, ls.get(1).getDateSent());
    }

    @Test
    @DisplayName("Debe mappear de entidad a modelo")
    void shouldMapBetweenEntityAndModel() {
        nRepo.save(n1);
        assertNull(nMap.toModel(null));
        List<Notification> ls = nRepo.findByReceiverId(n1.getReceiverId());

        List<NotificationModel> modelList = ls.stream().map(nMap::toModel).collect(Collectors.toList());

        Notification saved = ls.get(0);
        NotificationModel model = modelList.get(0);

        assertEquals(saved.getId(), model.getId());
        assertEquals(saved.getReceiverId(), model.getReceiverId());
        assertEquals(saved.getTitle(), model.getTitle());
        assertEquals(saved.getDescription(), model.getDescription());
        assertEquals(saved.getState(), model.getState());
        assertEquals(saved.getDateCreated(), model.getDateCreated());
        assertEquals(saved.getDateSent(), model.getDateSent());
        assertEquals(saved.getRequestId(), model.getRequestId());
    }

    @Test
    @DisplayName("Debe mappear de modelo a entidad")
    void shouldMapBetweenModelAndEntity() {
        UUID n2RecId = UUID.randomUUID();
        UUID n2ReqId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime sent = LocalDateTime.now().plusSeconds(2);
        NotificationModel n2 = new NotificationModel();
        n2.setReceiverId(n2RecId);
        n2.setRequestId(n2ReqId);
        n2.setDateCreated(created);
        n2.setDateSent(sent);
        n2.setState(NotificationType.INCOMING_REQUEST);
        n2.setTitle("Solicitu Entrante");
        n2.setDescription("Nueva reparación cerca de tí");

        assertNull(nMap.toEntity(null));
        Notification entity = nMap.toEntity(n2);

        Notification saved = nRepo.save(entity);
        NotificationModel model = n2;

        assertNotNull(saved.getId());
        assertEquals(saved.getReceiverId(), model.getReceiverId());
        assertEquals(saved.getTitle(), model.getTitle());
        assertEquals(saved.getDescription(), model.getDescription());
        assertEquals(saved.getState(), model.getState());
        assertEquals(saved.getDateCreated(), model.getDateCreated());
        assertEquals(saved.getDateSent(), model.getDateSent());
        assertEquals(saved.getRequestId(), model.getRequestId());
    }

    @Test
    @DisplayName("Debe eliminar una notificacion")
    void shouldDeleteNotification() {
        Notification saved = nRepo.save(n1);

        nRepo.deleteById(saved.getId());

        List<Notification> ls = nRepo.findByReceiverId(saved.getReceiverId());

        assertFalse(ls.contains(saved));

        Optional<Notification> optional = nRepo.findById(saved.getId());

        assertTrue(optional.isEmpty());
    }

    @Test
    @DisplayName("Debe actualizar una notificacion")
    void shouldUpdateNotification() {
        Notification saved = nRepo.save(n1);

        Optional<Notification> found = nRepo.findById(saved.getId());

        assertFalse(found.isEmpty());

        Notification modifying = found.orElseThrow();
        modifying.setTitle("Solicitud no enviada");

        nRepo.save(modifying);

        Optional<Notification> newFound = nRepo.findById(modifying.getId());

        assertFalse(newFound.isEmpty());
        assertEquals(modifying.getTitle(), newFound.orElseThrow().getTitle());
    }

    @Test
    @DisplayName("Debe encontrar las notificaciones a partir de la id de request")
    void shouldFindByRequestId() {
        UUID n1RecId = n1.getReceiverId();
        UUID n1ReqId = n1.getRequestId();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime sent = LocalDateTime.now().plusSeconds(2);
        Notification n2 = new Notification();
        n2.setReceiverId(n1RecId);
        n2.setRequestId(n1ReqId);
        n2.setDateCreated(created);
        n2.setDateSent(sent);
        n2.setState(NotificationType.INCOMING_REQUEST);
        n2.setTitle("Solicitu Entrante");
        n2.setDescription("Nueva reparación cerca de tí");

        Notification saved = nRepo.save(n1);
        Notification saved2 = nRepo.save(n2);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(n1.getTitle(), saved.getTitle());
        assertEquals(n1.getDescription(), saved.getDescription());
        assertEquals(n1.getReceiverId(), saved.getReceiverId());
        assertEquals(n1.getRequestId(), saved.getRequestId());

        List<Notification> ls = nRepo.findByRequestIdAndReceiverId(n1ReqId, saved.getReceiverId());

        assertEquals(2, ls.size());
        assertTrue(ls.contains(saved));
        assertTrue(ls.contains(saved2));
        assertEquals(ls.get(0).getReceiverId(), n1.getReceiverId());
        assertEquals(ls.get(1).getReceiverId(), n1.getReceiverId());
    }
}