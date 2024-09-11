package yuquiz.domain.pinnedQuiz.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.pinnedQuiz.api.PinnedQuizApi;
import yuquiz.domain.pinnedQuiz.dto.PinnedQuizSortType;
import yuquiz.domain.pinnedQuiz.service.PinnedQuizService;
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class PinnedQuizController implements PinnedQuizApi {
    private final PinnedQuizService pinnedQuizService;

    @GetMapping("/pinned")
    public ResponseEntity<?> getPinnedQuizzes(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "sort", defaultValue = "LIKED_DATE_DESC") PinnedQuizSortType sort) {

        Page<QuizSummaryRes> pinnedQuizzes = pinnedQuizService.getPinnedQuizzes(userDetails.getId(), page, sort);
        return ResponseEntity.status(HttpStatus.OK).body(pinnedQuizzes);
    }

    @PostMapping("/{quizId}/pin")
    public ResponseEntity<?> pinQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId) {

        pinnedQuizService.pinQuiz(userDetails.getId(), quizId);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("성공적으로 추가되었습니다."));
    }

    @DeleteMapping("/{quizId}/pin")
    public ResponseEntity<?> deletePinQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId) {

        pinnedQuizService.deletePinQuiz(userDetails.getId(), quizId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
