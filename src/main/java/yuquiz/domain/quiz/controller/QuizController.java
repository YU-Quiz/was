package yuquiz.domain.quiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.quiz.dto.QuizCreateReq;
import yuquiz.domain.quiz.service.QuizService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateReq quizCreateReq, Principal principal) {
        quizService.quizCreate(quizCreateReq,principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("퀴즈 생성 성공."));
    }
}
