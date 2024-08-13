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
import yuquiz.domain.post.dto.PostSortType;

@Tag(name = "[관리자용 게시글 API]", description = "관리자용 게시글 관련 API")
public interface AdminPostApi {

    @Operation(summary = "전체 게시글 페이지별 조회", description = "전체 게시글을 정렬 기준에 따라 페이지별로 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
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
                                    "postId": 2,
                                    "postTitle": "게시글 제목",
                                    "nickname": "테스터",
                                    "createdAt": "2024-08-12T01:10:10"
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
    ResponseEntity<?> getAllPosts(@RequestParam PostSortType sort,
                                  @RequestParam @Min(0) Integer page);

    @Operation(summary = "특정 게시글 삭제", description = "게시글id를 기반으로 게시글을 삭제하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    })
    ResponseEntity<?> deletePost(@PathVariable("postId") Long postId);
}
