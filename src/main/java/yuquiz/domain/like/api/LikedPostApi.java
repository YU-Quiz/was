package yuquiz.domain.like.api;

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

@Tag(name = "[게시글 좋아요 API]", description = "게시글 좋아요 관련 API")
public interface LikedPostApi {

    @Operation(summary = "좋아요 게시글 목록", description = "사용자가 좋아요한 게시글 목록 API")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 목록 불러오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                            {
                                                 "totalPages": 1,
                                                 "totalElements": 4,
                                                 "first": true,
                                                 "last": true,
                                                 "size": 20,
                                                 "content": [
                                                     {
                                                         "postId": 4,
                                                         "postTitle": "새로운 타이틀",
                                                         "nickname": "테스터111",
                                                         "categoryName": "자유게시판",
                                                         "createdAt": "2024-08-20T14:57:51.031651",
                                                         "likeCount": 0,
                                                         "viewCount": 0
                                                     },
                                                     {
                                                         "postId": 6,
                                                         "postTitle": "새로운 타이틀",
                                                         "nickname": "테스터",
                                                         "categoryName": "자유게시판",
                                                         "createdAt": "2024-08-20T15:33:34.426304",
                                                         "likeCount": 0,
                                                         "viewCount": 0
                                                     },
                                                     {
                                                         "postId": 12,
                                                         "postTitle": "qwe12",
                                                         "nickname": "test1111",
                                                         "categoryName": "공지게시판",
                                                         "createdAt": "2024-08-22T18:28:30.319814",
                                                         "likeCount": 1,
                                                         "viewCount": 0
                                                     },
                                                     {
                                                         "postId": 8,
                                                         "postTitle": "새로운 타이틀",
                                                         "nickname": "테스터",
                                                         "categoryName": "자유게시판",
                                                         "createdAt": "2024-08-20T15:33:46.902076",
                                                         "likeCount": 1,
                                                         "viewCount": 0
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
                                                 "numberOfElements": 4,
                                                 "empty": false
                                            }
                                    """)
                    }))
    )
    ResponseEntity<?> getLikedPosts(@RequestParam(value = "page") @Min(0) Integer page,
                                    @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 설정하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "게시글 좋아요 성공"
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "이미 좋아요한 게시글",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 409,
                                        "message": "이미 좋아요한 게시글 입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글, 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "존재하지 않는 퀴즈", value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 게시글입니다."
                                    }
                                    """),
                            @ExampleObject(name = "존재하지 않는 사용자", value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> likePost(@PathVariable(value = "postId") Long postId,
                               @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 좋아요 취소", description = "게시글에 좋아요를 취소 설정하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "좋아요 취소 성공")
    })
    ResponseEntity<?> deleteLikePost(@PathVariable(value = "postId") Long postId,
                                     @AuthenticationPrincipal SecurityUserDetails userDetails);
}
