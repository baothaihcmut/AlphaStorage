package vn.anpha.storage.Notification.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import vn.anpha.storage.User.Entity.User;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @Column(name = "notification_id")
    private String notificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String actionUrl;

    @ManyToOne()
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne()
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "enum('UNREAD', 'READ') default 'UNREAD'")
    private NotificationStatus status;

    @Column(columnDefinition = "enum('INVITECOMPANY', 'ACCEPTINVITECOMPANY', 'REJECT', 'DELETE', 'UPDATE') default 'INVITECOMPANY'")
    private NotificationType type;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;

}
