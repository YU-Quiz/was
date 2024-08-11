package yuquiz.domain.post.api;

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

@Tag(name = "[관리자용 게시글 API]", description = "관리자용 게시글 관련 API")
public interface AdminPostApi {

    @Operation(summary = "전체 게시글 페이지별 최신순 조회", description = "전체 게시글을 페이지별로 조회하는 API입니다. 게시글은 최신순으로 조회됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "totalPages": 1,
                            "totalElements": 1,
                            "first": true,
                            "last": true,
                            "size": 10,
                            "content": [
                                {
                                    "postId": 2,
                                    "postTitle": "게시글 제목",
                                    "nickname": "테스터",
                                    "createdAt": "2024-08-12T01:10:10"
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
                                "pageSize": 10,
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
    })
    ResponseEntity<?> getLatestPostsByPage(@RequestParam @Min(0) Integer page);

    @Operation(summary = "특정 게시글 삭제", description = "게시글id를 기반으로 게시글을 삭제하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    })
    ResponseEntity<?> deletePost(@PathVariable("postId") Long postId);
}
