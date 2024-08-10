package yuquiz.domain.quiz.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.domain.quiz.dto.QuizRes;
import yuquiz.domain.quiz.service.AdminQuizService;

@RestController
@RequestMapping("/api/v1/admin/quizzes")
@RequiredArgsConstructor
public class AdminQuizController {

    private final AdminQuizService adminQuizService;

    @GetMapping("/")
    public ResponseEntity<?> getQuizPage(@RequestParam @Min(0) Integer pageNumber){

        Page<QuizRes> page = adminQuizService.getQuizPage(pageNumber);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
