package oficioya_notification.service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FcmServiceInitializedTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void sendPushInitialized() throws Exception {
        FcmService service = new FcmService();
        setField(service, "initialized", true);
        try (MockedStatic<FirebaseMessaging> mockedFm = Mockito.mockStatic(FirebaseMessaging.class);
             MockedStatic<Notification> mockedNotif = Mockito.mockStatic(Notification.class)) {
            FirebaseMessaging fm = Mockito.mock(FirebaseMessaging.class);
            Mockito.when(fm.send(Mockito.any(Message.class))).thenReturn("msg-id");
            mockedFm.when(FirebaseMessaging::getInstance).thenReturn(fm);

            Notification.Builder notifBuilder = Mockito.mock(Notification.Builder.class);
            Mockito.when(notifBuilder.setTitle(Mockito.anyString())).thenReturn(notifBuilder);
            Mockito.when(notifBuilder.setBody(Mockito.anyString())).thenReturn(notifBuilder);
            Mockito.when(notifBuilder.build()).thenReturn(Mockito.mock(Notification.class));
            mockedNotif.when(Notification::builder).thenReturn(notifBuilder);

            assertDoesNotThrow(() -> service.sendPush("token", null, "title", "body", java.util.Map.of()));
        }
    }
}