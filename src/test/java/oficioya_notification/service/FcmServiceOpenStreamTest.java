package oficioya_notification.service;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FcmServiceOpenStreamTest {

    @Test
    void openFilesystemStream() throws Exception {
        Path tmp = Files.createTempFile("cred", ".json");
        Files.writeString(tmp, "{}");
        FcmService service = new FcmService();
        Method m = service.getClass().getDeclaredMethod("openCredentialsStream", String.class);
        m.setAccessible(true);
        InputStream is = (InputStream) m.invoke(service, tmp.toString());
        assertNotNull(is);
        is.close();
        Files.delete(tmp);
    }
}