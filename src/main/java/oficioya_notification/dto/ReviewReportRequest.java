package oficioya_notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewReportRequest(
    String reviewId,
    String reportedBy,
    String reason,
    String reviewContent,
    LocalDateTime reportedAt
) {
}