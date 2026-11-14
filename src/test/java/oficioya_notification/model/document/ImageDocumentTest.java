package oficioya_notification.model.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ImageDocumentTest {

    @Test
    @DisplayName("Debe construir y modificar correctamente ImageDocument")
    void shouldCreateAndMutateImageDocument() {
        UUID id = UUID.randomUUID();
        UUID notifId = UUID.randomUUID();
        LocalDateTime fecha = LocalDateTime.now();
        byte[] bytes = new byte[]{1, 2, 3};

        ImageDocument doc = new ImageDocument("foto.png", "image/png", 3L, bytes, fecha, "ref-123");
        doc.setId(id);
        doc.setNotificationId(notifId);

        assertEquals(id, doc.getId());
        assertEquals(notifId, doc.getNotificationId());
        assertEquals("foto.png", doc.getNombre());
        assertEquals("image/png", doc.getTipoContenido());
        assertEquals(3L, doc.getTamano());
        assertArrayEquals(bytes, doc.getDatos());
        assertEquals(fecha, doc.getFechaCarga());
        assertEquals("ref-123", doc.getReferenciaExterna());

        ImageDocument emptyDoc = new ImageDocument();
        emptyDoc.setNombre("otra.jpg");
        emptyDoc.setTipoContenido("image/jpeg");
        emptyDoc.setTamano(10L);
        emptyDoc.setDatos(new byte[]{4, 5});
        emptyDoc.setFechaCarga(fecha);
        emptyDoc.setReferenciaExterna("ref-456");

        assertEquals("otra.jpg", emptyDoc.getNombre());
        assertEquals("image/jpeg", emptyDoc.getTipoContenido());
        assertEquals(10L, emptyDoc.getTamano());
        assertArrayEquals(new byte[]{4, 5}, emptyDoc.getDatos());
        assertEquals(fecha, emptyDoc.getFechaCarga());
        assertEquals("ref-456", emptyDoc.getReferenciaExterna());
    }
}