package yuquiz.domain.report.dto;

import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.entity.ReportType;


public record ReportRes(
        Long reportId,
        Long quizId,
        String reason,
        ReportType type
) {
    public static ReportRes from(Report report) {
        return new ReportRes(
                report.getId(),
                report.getQuiz().getId(),
                report.getReason(),
                report.getReportType()
        );
    }
}
