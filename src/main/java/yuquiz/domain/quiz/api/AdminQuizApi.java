package yuquiz.domain.quiz.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import yuquiz.domain.quiz.dto.QuizSortType;

@Tag(name = "[관리자용 퀴즈 API]", description = "관리자용 퀴즈 관련 API")
public interface AdminQuizApi {

    @Operation(summary = "전체 퀴즈 페이지별 조회", description = "전체 퀴즈를 정렬 기준에 따라 페이지별로 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 조회 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "totalPages": 1,
                            "totalElements": 3,
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
                                    "viewCount": 15
                                },
                                {
                                    "quizId": 8,
                                    "quizTitle": "hello",
                                    "nickname": "테스터",
                                    "createdAt": "2024-08-12T13:32:55",
                                    "likeCount": 2,
                                    "viewCount": 20
                                },
                                {
                                    "quizId": 9,
                                    "quizTitle": "testtest",
                                    "nickname": "admin",
                                    "createdAt": "2024-08-12T13:33:45",
                                    "likeCount": 10,
                                    "viewCount": 222
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
                            "numberOfElements": 3,
                            "empty": false
                        }
                    """)
            }))
    })
    ResponseEntity<?> getAllQuizzes(@RequestParam QuizSortType sort,
                                    @RequestParam @Min(0) Integer page);

    @Operation(summary = "특정 퀴즈 삭제", description = "퀴즈id를 기반으로 퀴즈를 삭제하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "퀴즈 삭제 성공")
    })
    ResponseEntity<?> deleteQuiz(@PathVariable("quizId") Long quizId);
}
