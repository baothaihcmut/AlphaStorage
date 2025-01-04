package vn.anpha.storage.Notification.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.anpha.storage.Notification.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(value = """
    SELECT
        noti.notification_id,
        noti.action_url,
        noti.content,
        noti.create_at,
        noti.status,
        noti.title,
        noti.type,
        noti.updated_at,
        noti.recipient_id,
        noti.sender_id
    FROM notifications noti
    WHERE noti.recipient_id = :userId
    """,
            countQuery = """
        SELECT COUNT(*) FROM notifications noti
        WHERE noti.recipient_id = :userId
    """,
            nativeQuery = true)
    Page<Notification> findNotificationByUserId(@Param("userId") String userId, Pageable pageable);


}