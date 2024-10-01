package yuquiz.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.report.api.ReportApi;
import yuquiz.domain.report.dto.ReportReq;
import yuquiz.domain.report.service.ReportService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReportController implements ReportApi {
    private final ReportService reportService;

    @PostMapping("/quizzes/{quizId}/report")
    public ResponseEntity<?> reportQuiz(@PathVariable(value = "quizId") Long quizId,
                                        @Valid @RequestBody ReportReq reportReq,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails) {
        reportService.reportQuiz(quizId, reportReq, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("퀴즈 신고 완료."));
    }
}
