package yuquiz.domain.post.api;

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
import yuquiz.domain.post.dto.PostReq;
import yuquiz.domain.post.dto.PostSortType;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[게시글 API]", description = "게시글 관련 API")
public interface PostApi {

    @Operation(summary = "게시글 생성", description = "게시글을 생성하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 생성 성공",
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
                                        "title": "제목은 필수 입력입니다.",
                                        "content": "내용은 필수 입력 입니다.",
                                        "categoryId": "카테고리는 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    public ResponseEntity<?> createPost(@Valid @RequestBody PostReq postReq,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 조회", description = "게시글 ID로 게시글을 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "title": "새로운 타이틀",
                                        "content": "새로운 컨텐츠",
                                        "category": "자유게시판",
                                        "nickname": "테스터111",
                                        "likeCount": 0,
                                        "viewCount": 0,
                                        "createdAt": "2024-08-20T14:57:51.031651",
                                        "modified": true
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 게시글 입니다."
                                    }
                                    """)
                    })),

    })
    public ResponseEntity<?> getPostById(@PathVariable(value = "postId") Long postId);

    @Operation(summary = "게시글 수정", description = "게시글을 수정하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "게시글 수정 성공."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "403", description = "작성자 불일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 401,
                                        "message": "권한이 없습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "title": "제목은 필수 입력입니다.",
                                        "content": "내용은 필수 입력 입니다.",
                                        "categoryId": "카테고리는 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    public ResponseEntity<?> updatePost(@PathVariable(value = "postId") Long postId,
                                        @Valid @RequestBody PostReq postReq,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 관련 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "작성자 불일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 401,
                                        "message": "권한이 없습니다."
                                    }
                                    """)
                    }))
    })
    public ResponseEntity<?> deletePost(@PathVariable(value = "postId") Long postId,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 검색", description = "키워드를 이용해 게시글을 조회하는 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                          "totalPages": 1,
                                          "totalElements": 5,
                                          "first": true,
                                          "last": true,
                                          "size": 20,
                                          "content": [
                                              {
                                                  "postId": 11,
                                                  "postTitle": "제목1",
                                                  "nickname": "테스터",
                                                  "createdAt": "2024-08-21T02:19:39.775264",
                                                  "likeCount": 0,
                                                  "viewCount": 0
                                              },
                                              {
                                                  "postId": 9,
                                                  "postTitle": "2제목",
                                                  "nickname": "테스터",
                                                  "createdAt": "2024-08-20T15:33:51.620851",
                                                  "likeCount": 0,
                                                  "viewCount": 0
                                              },
                                              {
                                                  "postId": 8,
                                                  "postTitle": "1제목",
                                                  "nickname": "테스터",
                                                  "createdAt": "2024-08-20T15:33:46.902076",
                                                  "likeCount": 0,
                                                  "viewCount": 0
                                              },
                                              {
                                                  "postId": 7,
                                                  "postTitle": "1제목",
                                                  "nickname": "테스터",
                                                  "createdAt": "2024-08-20T15:33:43.375318",
                                                  "likeCount": 0,
                                                  "viewCount": 0
                                              },
                                              {
                                                  "postId": 6,
                                                  "postTitle": "제목1",
                                                  "nickname": "테스터",
                                                  "createdAt": "2024-08-20T15:33:34.426304",
                                                  "likeCount": 0,
                                                  "viewCount": 0
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
                                          "numberOfElements": 5,
                                          "empty": false
                                      }
                                    """)
                    })),
    })
    public ResponseEntity<?> getPostsByKeyword(@RequestParam(value = "keyword") String keyword,
                                               @RequestParam(value = "sort") PostSortType sort,
                                               @RequestParam(value = "page") @Min(0) Integer page);

    @Operation(summary = "카테고리별 게시글 조회", description = "카테고리 이름과 정렬 기준에 따라 게시글 페이지를 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "totalPages": 1,
                                        "totalElements": 5,
                                        "first": true,
                                        "last": true,
                                        "size": 20,
                                        "content": [
                                            {
                                                "postId": 11,
                                                "postTitle": "제목1",
                                                "nickname": "테스터",
                                                "createdAt": "2024-08-21T02:19:39.775264",
                                                "likeCount": 0,
                                                "viewCount": 0
                                            },
                                            {
                                                "postId": 9,
                                                "postTitle": "2제목",
                                                "nickname": "테스터",
                                                "createdAt": "2024-08-20T15:33:51.620851",
                                                "likeCount": 0,
                                                "viewCount": 0
                                            },
                                            {
                                                "postId": 8,
                                                "postTitle": "1제목",
                                                "nickname": "테스터",
                                                "createdAt": "2024-08-20T15:33:46.902076",
                                                "likeCount": 0,
                                                "viewCount": 0
                                            },
                                            {
                                                "postId": 7,
                                                "postTitle": "1제목",
                                                "nickname": "테스터",
                                                "createdAt": "2024-08-20T15:33:43.375318",
                                                "likeCount": 0,
                                                "viewCount": 0
                                            },
                                            {
                                                "postId": 6,
                                                "postTitle": "제목1",
                                                "nickname": "테스터",
                                                "createdAt": "2024-08-20T15:33:34.426304",
                                                "likeCount": 0,
                                                "viewCount": 0
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
                                        "numberOfElements": 5,
                                        "empty": false
                                    }
                                    """)

                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 카테고리입니다."
                                    }
                                    """)
                    }))
    })
    public ResponseEntity<?> getPostsByCategory(@PathVariable(value = "categoryName") String categoryName,
                                                @RequestParam(value = "sort") PostSortType sort,
                                                @RequestParam(value = "page") @Min(0) Integer page);
}
