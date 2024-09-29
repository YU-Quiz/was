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
import yuquiz.domain.quiz.dto.quiz.AnswerReq;
import yuquiz.domain.quiz.dto.quiz.QuizReq;
import yuquiz.domain.quiz.dto.quiz.QuizSortType;
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
                                        "title": "hello",
                                        "question": "hihi",
                                        "quizImg": null,
                                        "quizType": "MULTIPLE_CHOICE",
                                        "likeCount": 3,
                                        "viewCount": 42,
                                        "choices": [
                                            "1",
                                            "3",
                                            "4"
                                        ],
                                        "subject": "마이크로프로세서",
                                        "writer": "테스터",
                                        "createdAt": "2024-08-12T13:32:55",
                                        "isLiked": true,
                                        "isPinned": false,
                                        "isWriter": true
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
                            @ExampleObject(name = "정답", value = """
                                    {
                                        "response": true
                                    }
                                    """),
                            @ExampleObject(name = "오답", value = """
                                    {
                                        "response": false
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
                                         "totalElements": 10,
                                         "first": true,
                                         "last": true,
                                         "size": 20,
                                         "content": [
                                             {
                                                 "quizId": 35,
                                                 "quizTitle": "진짜ㅏ",
                                                 "nickname": "지원",
                                                 "createdAt": "2024-09-19T16:32:08.237412",
                                                 "likeCount": 0,
                                                 "viewCount": 4,
                                                 "isSolved": null,
                                                 "quizType": "TRUE_FALSE"
                                             },
                                             {
                                                 "quizId": 32,
                                                 "quizTitle": "tjsdks",
                                                 "nickname": "관리자",
                                                 "createdAt": "2024-09-19T15:47:50.017553",
                                                 "likeCount": 0,
                                                 "viewCount": 16,
                                                 "isSolved": null,
                                                 "quizType": "MULTIPLE_CHOICE"
                                             },
                                             {
                                                 "quizId": 31,
                                                 "quizTitle": "jiwon",
                                                 "nickname": "테스터",
                                                 "createdAt": "2024-09-19T15:46:35.345603",
                                                 "likeCount": 0,
                                                 "viewCount": 10,
                                                 "isSolved": null,
                                                 "quizType": "SHORT_ANSWER"
                                             },
                                             {
                                                 "quizId": 30,
                                                 "quizTitle": "11",
                                                 "nickname": "테스터",
                                                 "createdAt": "2024-09-19T15:40:42.694588",
                                                 "likeCount": 0,
                                                 "viewCount": 14,
                                                 "isSolved": null,
                                                 "quizType": "TRUE_FALSE"
                                             },
                                             {
                                                 "quizId": 29,
                                                 "quizTitle": "하나 둘 삼",
                                                 "nickname": "관리자",
                                                 "createdAt": "2024-09-19T13:27:25.505916",
                                                 "likeCount": 0,
                                                 "viewCount": 13,
                                                 "isSolved": null,
                                                 "quizType": "SHORT_ANSWER"
                                             },
                                             {
                                                 "quizId": 28,
                                                 "quizTitle": "단답식 테스트입니다.",
                                                 "nickname": "관리자",
                                                 "createdAt": "2024-09-19T13:22:04.228084",
                                                 "likeCount": 0,
                                                 "viewCount": 8,
                                                 "isSolved": null,
                                                 "quizType": "SHORT_ANSWER"
                                             },
                                             {
                                                 "quizId": 26,
                                                 "quizTitle": "구자욱 오늘 홈런쳤나요>?",
                                                 "nickname": "관리자",
                                                 "createdAt": "2024-09-14T16:51:53.768297",
                                                 "likeCount": 0,
                                                 "viewCount": 68,
                                                 "isSolved": null,
                                                 "quizType": "TRUE_FALSE"
                                             },
                                             {
                                                 "quizId": 15,
                                                 "quizTitle": "hello",
                                                 "nickname": "테스터",
                                                 "createdAt": "2024-08-12T13:32:55",
                                                 "likeCount": 3,
                                                 "viewCount": 42,
                                                 "isSolved": false,
                                                 "quizType": "MULTIPLE_CHOICE"
                                             },
                                             {
                                                 "quizId": 7,
                                                 "quizTitle": "퀴즈 테스트",
                                                 "nickname": "테스터",
                                                 "createdAt": "2024-08-11T19:43:53",
                                                 "likeCount": 5,
                                                 "viewCount": 25,
                                                 "isSolved": true,
                                                 "quizType": "MULTIPLE_CHOICE"
                                             },
                                             {
                                                 "quizId": 16,
                                                 "quizTitle": "testing",
                                                 "nickname": "테스터111",
                                                 "createdAt": "2024-08-11T19:43:53",
                                                 "likeCount": 5,
                                                 "viewCount": 24,
                                                 "isSolved": null,
                                                 "quizType": "MULTIPLE_CHOICE"
                                             }
                                         ],
                                         "number": 0,
                                         "sort": {
                                             "empty": false,
                                             "sorted": true,
                                             "unsorted": false
                                         },
                                         "pageable": {
                                             "pageNumber": 0,
                                             "pageSize": 20,
                                             "sort": {
                                                 "empty": false,
                                                 "sorted": true,
                                                 "unsorted": false
                                             },
                                             "offset": 0,
                                             "unpaged": false,
                                             "paged": true
                                         },
                                         "numberOfElements": 10,
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
}
