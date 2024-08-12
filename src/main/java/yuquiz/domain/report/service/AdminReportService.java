package yuquiz.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.report.dto.ReportSortType;
import yuquiz.domain.report.dto.ReportSummaryRes;
import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.repository.ReportRepository;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final ReportRepository reportRepository;

    private static final Integer REPORT_PER_PAGE = 20;

    public Page<ReportSummaryRes> getAllReports(ReportSortType sort, Integer page) {

        Pageable pageable = PageRequest.of(page, REPORT_PER_PAGE, sort.getSort());
        Page<Report> reports = reportRepository.findAll(pageable);

        return reports.map(ReportSummaryRes::fromEntity);
    }
}
