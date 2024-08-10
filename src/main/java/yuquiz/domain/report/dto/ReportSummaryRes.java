package yuquiz.domain.report.dto;

import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.entity.ReportType;


public record ReportSummaryRes(
        Long reportId,
        Long quizId,
        String reason,
        ReportType type
) {
    public static ReportSummaryRes fromEntity(Report report) {
        return new ReportSummaryRes(
                report.getId(),
                report.getQuiz().getId(),
                report.getReason(),
                report.getReportType()
        );
    }
}
