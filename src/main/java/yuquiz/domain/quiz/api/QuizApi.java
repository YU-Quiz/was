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
import yuquiz.domain.quiz.dto.AnswerReq;
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

    @Operation(summary = "퀴즈 삭제", description = "퀴즈 삭제 관련 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "퀴즈 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "작성자 불일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                {
                                    "status": 403,
                                    "message": "권한이 없습니다."
                                }
                                """)
                    }))
    })
    public ResponseEntity<?> deleteQuiz(@PathVariable(value = "quizId") Long quizId, @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "퀴즈 수정", description = "퀴즈 수정 관련 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                {
                                    "response": "퀴즈 수정 성공."
                                }
                                """)
                    })),
            @ApiResponse(responseCode = "403", description = "작성자 불일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                {
                                    "status": 403,
                                    "message": "권한이 없습니다."
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
    public ResponseEntity<?> updateQuiz(
            @PathVariable(value = "quizId") Long quizId,
            @Valid @RequestBody QuizReq quizReq,
            @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "특정 퀴즈 조회", description = "퀴즈를 풀기 위해 특정 퀴즈를 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 조회 성공",
                content = @Content(mediaType = "application/json", examples = {
                        @ExampleObject(value = """
                                {
                                    "title": "퀴즈 제목",
                                    "question": "퀴즈 질문",
                                    "quizImg": null,
                                    "quizType": "MULTIPLE_CHOICE",
                                    "likeCount": 0,
                                    "choices": [
                                        "1.뭐요",
                                        "2.뭘봐요",
                                        "3.아닌데"
                                    ],
                                    "subject": "마이크로프로세서",
                                    "writer": "테스터입니다",
                                    "createdAt": "2024-08-10T16:33:00"
                                }
                                """)
                })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈",
                content = @Content(mediaType = "application/json", examples = {
                        @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "message": "존재하지 않는 퀴즈입니다."
                                }
                                """)
                })),

    })
    public ResponseEntity<?> getQuizById(@PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 채점", description = "퀴즈를 풀고 채점하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채점 성공",
                content = @Content(mediaType = "application/json", examples = {
                        @ExampleObject(value = """
                                {
                                    true
                                }
                                """)
                })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈",
                content = @Content(mediaType = "application/json", examples = {
                        @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "message": "존재하지 않는 퀴즈입니다."
                                }
                                """)
                }))
    })
    public ResponseEntity<?> gradeQuiz(
            @PathVariable(value = "quizId") Long quizId,
            @Valid @RequestBody AnswerReq answerReq);


}
