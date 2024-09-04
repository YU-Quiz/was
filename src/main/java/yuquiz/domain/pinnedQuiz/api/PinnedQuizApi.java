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
import org.springframework.web.bind.annotation.PathVariable;
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
    ResponseEntity<?> pinQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);

    @Operation(summary = "퀴즈 즐겨찾기 취소", description = "퀴즈 즐겨찾기 취소 api")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "즐겨찾기 취소 성공"),
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
    ResponseEntity<?> deletePinQuiz(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable(value = "quizId") Long quizId);
}
