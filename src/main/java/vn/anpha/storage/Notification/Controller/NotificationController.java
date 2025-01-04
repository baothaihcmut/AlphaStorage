package vn.anpha.storage.Notification.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.anpha.storage.Notification.DTO.request.NotificationRequestDto;
import vn.anpha.storage.Notification.DTO.response.NotificationResponseDto;
import vn.anpha.storage.Notification.Entity.NotificationStatus;
import vn.anpha.storage.Notification.Service.NotificationService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Create notification
    @PostMapping
    public ApiResponseDto<NotificationResponseDto> createNotification(@RequestBody NotificationRequestDto requestDto) {
        NotificationResponseDto responseDto = notificationService.createNotification(requestDto);
        return ApiResponseDto.<NotificationResponseDto>builder()
                .result(responseDto)
                .build();
    }

    // Update notification
    @PutMapping("/{notificationId}")
    public ApiResponseDto<NotificationResponseDto> updateNotification(@PathVariable String notificationId,
            @RequestBody NotificationRequestDto requestDto) {
        NotificationResponseDto responseDto = notificationService.updateNotification(notificationId, requestDto);
        return ApiResponseDto.<NotificationResponseDto>builder()
                .result(responseDto)
                .build();
    }

    // Change status
    @PatchMapping("/{notificationId}/status")
    public ApiResponseDto<NotificationResponseDto> changeStatus(@PathVariable String notificationId,
            @RequestParam String status) {
        NotificationStatus notificationStatus = NotificationStatus.valueOf(status.toUpperCase());
        NotificationResponseDto responseDto = notificationService.changeStatus(notificationId, notificationStatus);
        return ApiResponseDto.<NotificationResponseDto>builder()
                .result(responseDto)
                .build();
    }

    // Delete notification
    @DeleteMapping("/{notificationId}")
    public ApiResponseDto<String> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponseDto.<String>builder()
                .result("Success Delete")
                .build();
    }

    // Get Notification
    @GetMapping()
    public ApiResponseDto<Page<NotificationResponseDto>> getNotifications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String sort) {
        return ApiResponseDto.<Page<NotificationResponseDto>>builder()
                .result(notificationService.getNotifications(page, size, sort))
                .build();
    }
}