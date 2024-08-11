package yuquiz.domain.report.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import yuquiz.domain.report.dto.ReportSummaryRes;
import yuquiz.domain.report.service.AdminReportService;

@RestController
@RequestMapping("/api/v1/admin/report")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping
    public ResponseEntity<?> getReportPage(@RequestParam @Min(0) Integer page) {

        Page<ReportSummaryRes> reports = adminReportService.getReportPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(reports);
    }
}
