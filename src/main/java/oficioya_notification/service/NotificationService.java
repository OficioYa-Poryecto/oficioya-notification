package oficioya_notification.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import oficioya_notification.dto.NotificationRequest;
import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.entity.Notification;
import oficioya_notification.mapper.ResponseMapper;
import oficioya_notification.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ResponseMapper responseMapper;
    private final FcmService fcmService;

    public NotificationService(NotificationRepository notificationRepository, ResponseMapper responseMapper, FcmService fcmService) {
        this.notificationRepository = notificationRepository;
        this.responseMapper = responseMapper;
        this.fcmService = fcmService;
    }

    public List<NotificationResponse> findAllForUser(UUID userId) {
        return notificationRepository.findByReceiverId(userId).stream().map(responseMapper::toResponse).toList();
    }

    public Optional<NotificationResponse> findById(UUID id) {
        Optional<Notification> response = notificationRepository.findById(id);
        if (response.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(responseMapper.toResponse(response.orElse(null)));
        }
    }

    public List<NotificationResponse> findAllForARequest(UUID requestId, UUID receiverId) {
        return notificationRepository.findByRequestIdAndReceiverId(requestId, receiverId).stream().map(responseMapper::toResponse).toList();
    }

    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification noti = responseMapper.toNotification(request);
        noti.setDateSent(noti.getDateCreated());

        NotificationResponse response = responseMapper.toResponse(notificationRepository.save(noti));

        String deviceToken = request.deviceToken();
        String topic = (deviceToken == null || deviceToken.isBlank()) ? "user_" + request.receiverId() : null;
        fcmService.sendPush(deviceToken, topic,
                noti.getTitle(), noti.getDescription(),
                java.util.Map.of("type", String.valueOf(request.type()), "requestId", String.valueOf(request.requestId())));

        return response;
    }

    public boolean deleteNotification(UUID requestId) {
        if (!notificationRepository.existsById(requestId)) {
            return false;
        }
        notificationRepository.deleteById(requestId);
        return true;
    }
}