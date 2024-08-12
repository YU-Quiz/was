package yuquiz.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.entity.ReportType;

public record ReportReq(
        @NotBlank(message = "신고 사유는 필수 입력입니다.")
        String reason,
        @NotNull(message = "신고 유형은 필수 입력입니다.")
        ReportType type
) {
    public Report toEntity(Quiz quiz) {
        return Report.builder()
                .reason(this.reason)
                .reportType(this.type)
                .quiz(quiz)
                .build();
    }
}
