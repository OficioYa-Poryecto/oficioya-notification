package oficioya_notification.model.document;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageDocumentTest {

    @Test
    void setters() {
        ImageDocument d = new ImageDocument();
        UUID id = UUID.randomUUID();
        d.setId(id);
        d.setNotificationId(UUID.randomUUID());
        d.setNombre("img.png");
        d.setTipoContenido("image/png");
        d.setTamano(100L);
        d.setDatos(new byte[]{1,2,3});
        d.setFechaCarga(LocalDateTime.now());
        d.setReferenciaExterna("ref-1");

        assertEquals(id, d.getId());
        assertEquals("img.png", d.getNombre());
        assertNotNull(d.getFechaCarga());
    }

    @Test
    void constructor() {
        ImageDocument d = new ImageDocument("img.png", "image/png", 100L, new byte[]{1}, LocalDateTime.now(), "ref");
        assertEquals("img.png", d.getNombre());
        assertEquals(100L, d.getTamano());
    }
}