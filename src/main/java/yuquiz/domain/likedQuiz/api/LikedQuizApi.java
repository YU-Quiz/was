package yuquiz.domain.likedQuiz.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[퀴즈 API]", description = "퀴즈 관련 API")
public interface LikedQuizApi {
    @Operation(summary = "좋아요 퀴즈 목록", description = "사용자가 좋아요한 퀴즈 목록 api")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "좋아요 목록 불러오기 성공",
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
                                        "quizId": 15,
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
                                "numberOfElements": 1,
                                "empty": false
                            }
                            """)
            }))
    )
    ResponseEntity<?> getLikedQuizzes(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam(value = "page") @Min(0) Integer page);

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
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈, 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "존재하지 않는 퀴즈", value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 퀴즈입니다."
                                    }
                                    """),
                            @ExampleObject(name = "존재하지 않는 사용자", value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    })),
    })
    ResponseEntity<?> likeQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 좋아요 삭제", description = "퀴즈 좋아요 삭제 api")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈, 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "존재하지 않는 퀴즈", value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 퀴즈입니다."
                                    }
                                    """),
                            @ExampleObject(name = "존재하지 않는 사용자", value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    })),
    })
    ResponseEntity<?> deleteLikeQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);
}
