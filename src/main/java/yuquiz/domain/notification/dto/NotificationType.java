package yuquiz.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    QUIZ_REPORT_NOTIFICATION("퀴즈에 대한 신고", "퀴즈에 대한 신고가 있습니다.");

    private final String title;
    private final String message;
}
