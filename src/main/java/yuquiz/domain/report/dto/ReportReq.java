package yuquiz.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.entity.ReportType;
import yuquiz.domain.user.entity.User;

public record ReportReq(
        String reason,
        @NotNull(message = "신고 유형은 필수 입력입니다.")
        ReportType type
) {
    public Report toEntity(Quiz quiz, User user) {
        return Report.builder()
                .reason(this.reason)
                .reportType(this.type)
                .quiz(quiz)
                .user(user)
                .build();
    }
}
