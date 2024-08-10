package yuquiz.domain.report.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.domain.report.dto.ReportSummaryRes;
import yuquiz.domain.report.service.AdminReportService;

@RestController
@RequestMapping("/api/v1/admin/report")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping
    public ResponseEntity<?> getReportPage(@RequestParam @Min(0) Integer pageNumber) {

        Page<ReportSummaryRes> page = adminReportService.getReportPage(pageNumber);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
