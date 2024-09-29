package yuquiz.domain.quiz.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.domain.quiz.dto.quiz.QuizSummaryRes;
import yuquiz.domain.quiz.api.TriedQuizApi;
import yuquiz.domain.quiz.service.TriedQuizService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class TriedQuizController implements TriedQuizApi {
    private final TriedQuizService triedQuizService;

    @GetMapping("/incorrect")
    public ResponseEntity<?> getFailedQuizzes(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "page") @Min(0) Integer page) {

        Page<QuizSummaryRes> failedQuizzes = triedQuizService.getFailedQuizzes(userDetails.getId(), page);

        return ResponseEntity.status(HttpStatus.OK).body(failedQuizzes);
    }

    @GetMapping("/correct")
    public ResponseEntity<?> getSucceedQuizzes(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "page") @Min(0) Integer page) {

        Page<QuizSummaryRes> succeedQuizzes = triedQuizService.getSucceedQuizzes(userDetails.getId(), page);

        return ResponseEntity.status(HttpStatus.OK).body(succeedQuizzes);
    }
}
