package yuquiz.domain.quiz.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

@Tag(name = "[관리자용 퀴즈 API]", description = "관리자용 퀴즈 관련 API")
public interface AdminQuizApi {

    @Operation(summary = "전체 퀴즈 페이지별 최신순 조회", description = "전체 퀴즈를 페이지별로 조회하는 API입니다. 퀴즈는 최신순으로 조회됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 조회 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "totalPages": 1,
                            "totalElements": 2,
                            "first": true,
                            "last": true,
                            "size": 10,
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
                                    "quizId": 4,
                                    "quizTitle": "보지마세요",
                                    "nickname": "테스터다",
                                    "createdAt": "2024-08-10T16:33:00",
                                    "likeCount": 0,
                                    "viewCount": 1
                                }
                            ],
                            "number": 0,
                            "sort": {
                                "empty": true,
                                "sorted": false,
                                "unsorted": true
                            },
                            "numberOfElements": 2,
                            "pageable": {
                                "pageNumber": 0,
                                "pageSize": 10,
                                "sort": {
                                    "empty": true,
                                    "sorted": false,
                                    "unsorted": true
                                },
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                            },
                            "empty": false
                        }
                    """)
            }))
    })
    ResponseEntity<?> getQuizPage(@RequestParam @Min(0) Integer page);

    @Operation(summary = "특정 퀴즈 삭제", description = "퀴즈id를 기반으로 퀴즈를 삭제하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "퀴즈 삭제 성공")
    })
    ResponseEntity<?> deleteQuiz(@PathVariable("quizId") Long quizId);
}
