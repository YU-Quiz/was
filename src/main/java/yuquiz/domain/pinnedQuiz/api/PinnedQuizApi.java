package yuquiz.domain.pinnedQuiz.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[퀴즈 API]", description = "퀴즈 관련 API")
public interface PinnedQuizApi {
    @Operation(summary = "퀴즈 즐겨찾기 목록", description = "사용자가 즐겨찾기한 퀴즈 목록 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 목록 불러오기 성공",
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
                                        "viewCount": 16,
                                        "isSolved": true
                                    },
                                    {
                                        "quizId": 8,
                                        "quizTitle": "hello",
                                        "nickname": "테스터",
                                        "createdAt": "2024-08-12T13:32:55",
                                        "likeCount": 2,
                                        "viewCount": 20,
                                        "isSolved": false
                                    }
                                ],
                                "number": 0,
                                "sort": {
                                    "empty": true,
                                    "unsorted": true,
                                    "sorted": false
                                },
                                "pageable": {
                                    "pageNumber": 0,
                                    "pageSize": 20,
                                    "sort": {
                                        "empty": true,
                                        "unsorted": true,
                                        "sorted": false
                                    },
                                    "offset": 0,
                                    "unpaged": false,
                                    "paged": true
                                },
                                "numberOfElements": 2,
                                "empty": false
                            }
                            """)
            }))
    })
    ResponseEntity<?> getPinnedQuizzes(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "page") @Min(0) Integer page);
}
