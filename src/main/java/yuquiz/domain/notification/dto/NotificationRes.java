package yuquiz.domain.notification.dto;

import yuquiz.domain.notification.entity.Notification;

import java.time.LocalDateTime;

public record NotificationRes(
        Long id,

        String title,

        String message,

        boolean isChecked,

        String redirectUrl,

        LocalDateTime createdAt
) {
    public static NotificationRes fromEntity (Notification notification) {
        return new NotificationRes(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isChecked(),
                notification.getRedirectUrl(),
                notification.getCreatedAt()
        );
    }
}
