package oficioya_notification.mapper;

import org.springframework.stereotype.Component;

import oficioya_notification.dto.NotificationRequest;
import oficioya_notification.dto.NotificationResponse;
import oficioya_notification.entity.Notification;

@Component
public class ResponseMapper {
    
    public NotificationResponse toResponse(Notification notification){
        return new NotificationResponse(
            notification.getId(),
            notification.getTitle(),
            notification.getDescription(),
            notification.getState(),
            notification.getRequestId(),
            notification.getDateCreated(),
            notification.getDateSent()
        );
    }

    public Notification toNotification(NotificationRequest req){
        Notification notification =  new Notification();
<<<<<<< HEAD
        String message = req.message() != null ? req.message() : req.description();
        notification.setTitle(req.title() != null ? req.title() : (message != null ? message : "Notificación"));
        notification.setDescription(req.description() != null ? req.description() : (message != null ? message : ""));
        notification.setReceiverId(req.receiverId());
        notification.setState(req.type());
        notification.setRequestId(req.requestId());
        notification.setDateCreated(req.dateCreated() != null ? req.dateCreated() : java.time.LocalDateTime.now());
=======
        notification.setTitle(req.title());
        notification.setDescription(req.description());
        notification.setReceiverId(req.receiverId());
        notification.setState(req.type());
        notification.setRequestId(req.requestId());
        notification.setDateCreated(req.dateCreated());
>>>>>>> 84280b68168844e78a6ae713fd9da1f0af195d1c

        /*No se agrega ni el Id ni el tiempo del envío
         */
        return notification;
    }
}
