package oficioya_notification.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RolTest {

    @Test
    void values() {
        assertEquals(Rol.TRABAJADOR, Rol.valueOf("TRABAJADOR"));
    }
}