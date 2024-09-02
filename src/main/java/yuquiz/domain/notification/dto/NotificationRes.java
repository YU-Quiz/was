package yuquiz.domain.notification.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import yuquiz.domain.notification.entity.Notification;
import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

public record NotificationRes(

        String title,

        String message,

        boolean isChecked,

        String redirectUrl,

        LocalDateTime createdAt
) {
    public static NotificationRes fromEntity(Notification notification) {
        return new NotificationRes(
                notification.getTitle(),
                notification.getMessage(),
                notification.isChecked(),
                notification.getRedirectUrl(),
                notification.getCreatedAt()
        );
    }
}
