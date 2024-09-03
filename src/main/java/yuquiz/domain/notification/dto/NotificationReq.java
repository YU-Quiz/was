package yuquiz.domain.notification.dto;

import lombok.Builder;
import yuquiz.domain.notification.entity.Notification;
import yuquiz.domain.user.entity.User;

public record NotificationReq (
        String title,

        String message,

        String redirectUrl
){
    @Builder
    public NotificationReq(String title, String message, String redirectUrl) {
        this.title = title;
        this.message = message;
        this.redirectUrl = redirectUrl;
    }

    public Notification toEntity(User user) {
        return Notification.builder()
                .title(this.title)
                .message(this.message)
                .user(user)
                .redirectUrl(this.redirectUrl)
                .build();
    }
}
