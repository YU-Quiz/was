package yuquiz.domain.quiz.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[퀴즈 API]", description = "퀴즈 관련 API")
public interface QuizApi {
    @Operation(summary = "퀴즈 생성", description = "퀴즈를 생성하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 생성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                            {
                                "response": "퀴즈 생성 성공."
                            }
                            """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "notBlank", value = """
                                {
                                    "answer": "정답은 필수 입력입니다.",
                                    "question": "질문은 필수 입력입니다.",
                                    "title": "제목은 필수 입력입니다."
                                }
                                """),
                            @ExampleObject(name = "notNull", value = """
                                {
                                    "quizType": "퀴즈 유형은 필수 입력입니다.",
                                    "subjectId": "과목은 필수 입력입니다."
                                }
                                """)
                    }))
    })
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizReq quizReq, @AuthenticationPrincipal SecurityUserDetails userDetails);
}
