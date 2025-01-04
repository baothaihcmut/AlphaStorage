package vn.anpha.storage.Notification.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Notification.DTO.request.NotificationRequestDto;
import vn.anpha.storage.Notification.DTO.response.NotificationResponseDto;
import vn.anpha.storage.Notification.Entity.Notification;
import vn.anpha.storage.Notification.Entity.NotificationStatus;
import vn.anpha.storage.Notification.Repository.NotificationRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.respository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoticationService authoticationService;

    public NotificationResponseDto createNotification(NotificationRequestDto requestDto) {
        User recipient = userRepository.findById(requestDto.getRecipientId()).orElseThrow(() -> new RuntimeException("Recipient not found"));
        User sender = userRepository.findById(requestDto.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));

        Notification notification = new Notification();
        notification.setNotificationId(generateNotificationId());  
        notification.setTitle(requestDto.getTitle());
        notification.setContent(requestDto.getContent());
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setStatus(NotificationStatus.UNREAD);  
        notification.setType(requestDto.getType());
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

        return mapToResponseDto(notification);
    }

    public void deleteNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }

    public NotificationResponseDto updateNotification(String notificationId, NotificationRequestDto requestDto) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setTitle(requestDto.getTitle());
        notification.setContent(requestDto.getContent());
        notification.setType(requestDto.getType());

        notificationRepository.save(notification);

        return mapToResponseDto(notification);
    }
    
    public NotificationResponseDto changeStatus(String notificationId, NotificationStatus status) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(status);
        notificationRepository.save(notification);

        return mapToResponseDto(notification);
    }

    public Page<NotificationResponseDto> getNotifications(Integer page, Integer size, String sort) {
        User user = authoticationService.getUserByToken();

        // Define the sort direction
        Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create a Pageable object
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortDirection, "createdAt"));

        // Retrieve notifications
        Page<Notification> notificationPage =
                notificationRepository.findNotificationByUserId(user.getUserId(), pageRequest);

        // Map to DTOs
        return notificationPage.map(this::mapToResponseDto);
    }


    private String generateNotificationId() {
        return "notif-" + System.currentTimeMillis();
    }

    private NotificationResponseDto mapToResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .recipientId(notification.getRecipient().getUserId())
                .senderId(notification.getSender().getUserId())
                .status(notification.getStatus())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt().toString())
                .updatedAt(notification.getUpdatedAt().toString())
                .build();
    }


}
