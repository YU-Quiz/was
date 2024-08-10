package yuquiz.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.report.dto.ReportRes;
import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.repository.ReportRepository;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final ReportRepository reportRepository;

    private static final Integer REPORT_PER_PAGE = 10;

    public Page<ReportRes> getReportPage(Integer pageNumber){

        Pageable pageable = PageRequest.of(pageNumber, REPORT_PER_PAGE);
        Page<Report> page = reportRepository.findAllByOrderByCreatedAtDesc(pageable);

        return page.map(report -> ReportRes.from(report));
    }
}
