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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final ResponseMapper responseMapper;

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(NotificationRepository notificationRepository, ResponseMapper responseMapper){
        this.notificationRepository = notificationRepository;
        this.responseMapper = responseMapper;
    }

    public List<NotificationResponse> findAllForUser(UUID userId){
        log.info("Comenzando a buscar todas las notificaciones para un usuario con id: {}",userId);
        return notificationRepository.findByReceiverId(userId).stream().map(responseMapper::toResponse).toList();
    }

    public Optional<NotificationResponse> findById(UUID id){
        log.info("Buscar notificacion con id: {}", id);
        Optional<Notification> response = notificationRepository.findById(id);
        if(response.isEmpty()){
            log.debug("No encontrada");
            return Optional.empty();
        }
        else{
            log.debug("Encontrada");
            return Optional.of(responseMapper.toResponse(response.orElse(null)));
        }
    }

    public List<NotificationResponse> findAllForARequest(UUID requestId, UUID receiverId){
        log.info("Encontrar notificaciones que se han hecho en una request con id: {} que la ha recibido: {}",requestId,receiverId);
        /*
        Verificar si existe o no la request, si no existe, mandar exception y hacer un handler
         */
        return notificationRepository.findByRequestIdAndReceiverId(requestId, receiverId).stream().map(responseMapper::toResponse).toList();
    }

    public NotificationResponse sendNotification(NotificationRequest request){
        log.info("Enviar una notificacion");
        /*
        Lógica de conexión a firebase API para enviar una notificacion
         */

        Notification noti = responseMapper.toNotification(request);
        noti.setDateSent(noti.getDateCreated());

        return responseMapper.toResponse(notificationRepository.save(noti));
    }

    public boolean deleteNotification(UUID requestId){
        log.info("Eliminando una request con id: {}",requestId);
        if (!notificationRepository.existsById(requestId)){
            log.debug("No existe");
            return false;
        }
        log.debug("Se va a eliminar");
        notificationRepository.deleteById(requestId);
        log.debug("Se eliminó la notificacion");
        return true;
    }
    
}
