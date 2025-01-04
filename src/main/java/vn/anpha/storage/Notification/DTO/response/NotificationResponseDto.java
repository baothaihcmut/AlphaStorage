package vn.anpha.storage.Notification.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.anpha.storage.Notification.Entity.NotificationStatus;
import vn.anpha.storage.Notification.Entity.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private String notificationId;
    private String title;
    private String content;
    private String recipientId;
    private String senderId;
    private NotificationStatus status;
    private NotificationType type;
    private String createdAt;
    private String updatedAt;
}