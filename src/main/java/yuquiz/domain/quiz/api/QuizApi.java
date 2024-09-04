package yuquiz.domain.quiz.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import yuquiz.domain.quiz.dto.AnswerReq;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.domain.quiz.dto.QuizSortType;
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
                            @ExampleObject(value = """
                                    {
                                        "answer": "정답은 필수 입력입니다.",
                                        "question": "질문은 필수 입력입니다.",
                                        "title": "제목은 필수 입력입니다.",
                                        "quizType": "퀴즈 유형은 필수 입력입니다.",
                                        "subjectId": "과목은 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> createQuiz(@Valid @RequestBody QuizReq quizReq, @AuthenticationPrincipal SecurityUserDetails userDetails);

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
    ResponseEntity<?> deleteQuiz(@PathVariable(value = "quizId") Long quizId, @AuthenticationPrincipal SecurityUserDetails userDetails);

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
                            @ExampleObject(value = """
                                    {
                                        "answer": "정답은 필수 입력입니다.",
                                        "question": "질문은 필수 입력입니다.",
                                        "title": "제목은 필수 입력입니다.",
                                        "quizType": "퀴즈 유형은 필수 입력입니다.",
                                        "subjectId": "과목은 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> updateQuiz(
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
    ResponseEntity<?> getQuizById(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 채점", description = "퀴즈를 풀고 채점하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채점 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": true
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
    ResponseEntity<?> gradeQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId,
            @Valid @RequestBody AnswerReq answerReq);

    @Operation(summary = "퀴즈 정답 보기", description = "특정 퀴즈의 정답을 확인하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정답 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "1"
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
    ResponseEntity<?> getAnswer(@PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 검색", description = "퀴즈를 키워드 혹은 과목 별로 목록을 조회하는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "totalPages": 1,
                                        "totalElements": 2,
                                        "first": true,
                                        "last": true,
                                        "size": 20,
                                        "content": [
                                            {
                                                "quizId": 7,
                                                "quizTitle": "testing",
                                                "nickname": "테스터111",
                                                "createdAt": "2024-08-11T19:43:53",
                                                "likeCount": 5,
                                                "viewCount": 21,
                                                "isSolved": true
                                            },
                                            {
                                                "quizId": 16,
                                                "quizTitle": "testing",
                                                "nickname": "테스터111",
                                                "createdAt": "2024-08-11T19:43:53",
                                                "likeCount": 5,
                                                "viewCount": 18,
                                                "isSolved": null
                                            }
                                        ],
                                        "number": 0,
                                        "sort": {
                                            "empty": false,
                                            "sorted": true,
                                            "unsorted": false
                                        },
                                        "numberOfElements": 2,
                                        "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 20,
                                            "sort": {
                                                "empty": false,
                                                "sorted": true,
                                                "unsorted": false
                                            },
                                            "offset": 0,
                                            "paged": true,
                                            "unpaged": false
                                        },
                                        "empty": false
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> getQuizzesByKeywordAndSubject(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "subject", required = false) Long subjectId,
            @RequestParam(value = "sort") QuizSortType sort,
            @RequestParam(value = "page") @Min(0) Integer page);

    @Operation(summary = "작성한 퀴즈 목록", description = "사용자가 작성한 퀴즈 목록을 불러오는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "totalPages": 1,
                                        "totalElements": 1,
                                        "first": true,
                                        "last": true,
                                        "size": 20,
                                        "content": [
                                            {
                                                "quizId": 8,
                                                "quizTitle": "hello",
                                                "nickname": "테스터",
                                                "createdAt": "2024-08-12T13:32:55",
                                                "likeCount": 2,
                                                "viewCount": 20,
                                                "isSolved": true
                                            }
                                        ],
                                        "number": 0,
                                        "sort": {
                                            "empty": false,
                                            "unsorted": false,
                                            "sorted": true
                                        },
                                        "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 20,
                                            "sort": {
                                                "empty": false,
                                                "unsorted": false,
                                                "sorted": true
                                            },
                                            "offset": 0,
                                            "unpaged": false,
                                            "paged": true
                                        },
                                        "numberOfElements": 1,
                                        "empty": false
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> getQuizzesByWriter(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "sort") QuizSortType sort,
            @RequestParam(value = "page") @Min(0) Integer page);

    @Operation(summary = "퀴즈 즐겨찾기", description = "퀴즈 즐겨찾기 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "성공적으로 추가되었습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "퀴즈 즐겨찾기 중복",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 409,
                                        "message": "이미 즐겨찾기 한 퀴즈입니다."
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
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> pinQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 즐겨찾기 취소", description = "퀴즈 즐겨찾기 취소 api")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "즐겨찾기 취소 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 퀴즈입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> deletePinQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 좋아요", description = "퀴즈 좋아요 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 좋아요 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "성공적으로 추가되었습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "퀴즈 좋아요 중복",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 409,
                                        "message": "이미 좋아요 한 퀴즈입니다."
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
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> likeQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 좋아요 삭제", description = "퀴즈 좋아요 삭제 api")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 퀴즈입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> deleteLikeQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);
}
