package yuquiz.domain.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.common.entity.BaseTimeEntity;
import yuquiz.domain.notification.dto.NotificationType;
import yuquiz.domain.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String message;

    private boolean isChecked;

    @NotNull
    @Column(name = "redirect_url")
    private String redirectUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private NotificationType notificationType;

    @Builder
    public Notification(String title, String message, String redirectUrl, User user, NotificationType notificationType) {
        this.title = title;
        this.message = message;
        this.isChecked = false;
        this.redirectUrl = redirectUrl;
        this.user = user;
        this.notificationType = notificationType;
    }
}
