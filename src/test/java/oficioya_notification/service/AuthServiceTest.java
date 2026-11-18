package oficioya_notification.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthServiceTest {

    @Test
    void instantiate() {
        AuthService service = new AuthService();
        assertNotNull(service);
    }
}