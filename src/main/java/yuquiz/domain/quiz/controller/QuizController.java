package yuquiz.domain.quiz.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.quiz.api.QuizApi;
import yuquiz.domain.quiz.dto.AnswerReq;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.domain.quiz.dto.QuizSortType;
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.domain.quiz.service.QuizService;
import yuquiz.domain.report.dto.ReportReq;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController implements QuizApi {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizReq quizReq, @AuthenticationPrincipal SecurityUserDetails userDetails) {
        quizService.createQuiz(quizReq, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("퀴즈 생성 성공."));
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable(value = "quizId") Long quizId, @AuthenticationPrincipal SecurityUserDetails userDetails) {
        quizService.deleteQuiz(quizId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<?> updateQuiz(
            @PathVariable(value = "quizId") Long quizId,
            @Valid @RequestBody QuizReq quizReq,
            @AuthenticationPrincipal SecurityUserDetails userDetails) {
        quizService.updateQuiz(quizId, quizReq, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("퀴즈 수정 성공."));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuizById(@PathVariable(value = "quizId") Long quizId) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.getQuizById(quizId));
    }

    @PostMapping("/{quizId}/grade")
    public ResponseEntity<?> gradeQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId,
            @Valid @RequestBody AnswerReq answerReq) {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from(quizService.gradeQuiz(userDetails.getId(), quizId, answerReq.answer())));
    }

    @GetMapping("/{quizId}/answer")
    public ResponseEntity<?> getAnswer(@PathVariable(value = "quizId") Long quizId) {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from(quizService.getAnswer(quizId)));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<?> getQuizzesBySubject(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "subjectId") Long subjectId,
            @RequestParam(value = "page") @Min(0) Integer page,
            @RequestParam(value = "sort") QuizSortType sort) {
        Page<QuizSummaryRes> quizzes = quizService.getQuizzesBySubject(userDetails.getId(), subjectId, sort, page);
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }

    @GetMapping
    public ResponseEntity<?> getQuizzesByKeyword(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "sort") QuizSortType sort,
            @RequestParam(value = "page") @Min(0) Integer page) {
        Page<QuizSummaryRes> quizzes = quizService.getQuizzesByKeyword(userDetails.getId(), keyword, sort, page);
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }
}
