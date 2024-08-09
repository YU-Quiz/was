package yuquiz.domain.quiz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.domain.quiz.service.QuizService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    @PostMapping
    public ResponseEntity<?> createQuiz(@Valid  @RequestBody QuizReq quizReq, Principal principal) {
        quizService.createQuiz(quizReq,principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("퀴즈 생성 성공."));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteQuiz(@RequestParam(value = "quizId") Long quizId, Principal principal) {
        quizService.deleteQuiz(quizId, principal);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<?> updateQuiz(
            @RequestParam(value = "quizId") Long quizId,
            @RequestBody QuizReq quizReq,
            Principal principal) {
        quizService.updateQuiz(quizId, quizReq, principal);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("퀴즈 수정 성공."));
    }
}
