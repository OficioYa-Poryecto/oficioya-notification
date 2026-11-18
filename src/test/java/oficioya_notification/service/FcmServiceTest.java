package oficioya_notification.service;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FcmServiceTest {

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void initWithoutCredentials() {
        FcmService service = new FcmService();
        service.sendPush("token", null, "title", "body", Map.of());
    }

    @Test
    void sendWithNoTarget() {
        FcmService service = new FcmService();
        service.sendPush(null, null, "title", "body", Map.of());
    }

    @Test
    void sendWithTopic() {
        FcmService service = new FcmService();
        service.sendPush(null, "topic", "title", "body", Map.of());
    }

    @Test
    void initWithBadPath() throws Exception {
        FcmService service = new FcmService();
        setField(service, "credentialsPath", "nonexistent.json");
        setField(service, "projectId", "proj");
        java.lang.reflect.Method m = service.getClass().getDeclaredMethod("init");
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(service));
    }
}