package oficioya_notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import oficioya_notification.model.NotificationType;

public record NotificationRequest (
    UUID receiverId,
    String title,
    String description,
<<<<<<< HEAD
    String message,
    NotificationType type,
    UUID requestId,
    LocalDateTime dateCreated,
    String deviceToken
=======
    NotificationType type,
    UUID requestId,
    LocalDateTime dateCreated
>>>>>>> 84280b68168844e78a6ae713fd9da1f0af195d1c
    ){
    
}