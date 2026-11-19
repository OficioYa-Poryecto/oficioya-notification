package oficioya_notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import oficioya_notification.model.NotificationType;

public record NotificationRequest (
    UUID receiverId,
    String title,
    String description,
    String message,
    NotificationType type,
    UUID requestId,
    LocalDateTime dateCreated,
    String deviceToken
    ){
    
}
