package yuquiz.domain.likedQuiz.controller;

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
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.domain.likedQuiz.service.LikedQuizService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LikedQuizController {
    private final LikedQuizService likedQuizService;

    @GetMapping("/users/liked-quizzes")
    public ResponseEntity<?> getLikedQuizzes(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "page") @Min(0) Integer page) {

        Page<QuizSummaryRes> likedQuizzes = likedQuizService.getLikedQuizzes(userDetails.getId(), page);

        return ResponseEntity.status(HttpStatus.OK).body(likedQuizzes);
    }
}
