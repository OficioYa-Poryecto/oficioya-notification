package oficioya_notification.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReviewReportRequestTest {

    @Test
    void record() {
        ReviewReportRequest req = new ReviewReportRequest("r1", "u1", "reason", "content", LocalDateTime.now());
        assertEquals("r1", req.reviewId());
        assertNotNull(req.reportedAt());
    }
}