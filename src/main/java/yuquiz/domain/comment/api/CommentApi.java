package yuquiz.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import yuquiz.domain.comment.dto.CommentReq;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[댓글 API]", description = "게시글의 댓글 관련 API")
public interface CommentApi {

    @Operation(summary = "댓글 생성", description = "댓글을 생성하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "댓글 생성 성공."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "content": "내용은 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> createComment(@PathVariable(value = "postId") Long postId,
                                    @Valid @RequestBody CommentReq commentReq,
                                    @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "댓글 수정", description = "댓글을 수정하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "댓글 수정 성공."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "403", description = "작성자 불일치 및 없는 댓글",
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
                                        "content": "내용은 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> updateComment(@PathVariable(value = "commentId") Long commentId,
                                    @Valid @RequestBody CommentReq commentReq,
                                    @AuthenticationPrincipal SecurityUserDetails userDetails);

    ResponseEntity<?> deleteComment(@PathVariable(value = "commentId") Long commentId,
                                    @AuthenticationPrincipal SecurityUserDetails userDetails);
}
