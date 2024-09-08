package yuquiz.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.comment.dto.CommentReq;
import yuquiz.domain.comment.service.CommentService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable(value = "postId") Long postId,
                                           @Valid @RequestBody CommentReq commentReq,
                                           @AuthenticationPrincipal SecurityUserDetails userDetails) {

        commentService.createComment(commentReq, postId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("댓글 생성 성공"));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable(value = "commentId") Long commentId,
                                           @Valid @RequestBody CommentReq commentReq,
                                           @AuthenticationPrincipal SecurityUserDetails userDetails) {

        commentService.updateCommentById(commentReq, commentId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("댓글 수정 성공"));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "commentId") Long commentId,
                                           @AuthenticationPrincipal SecurityUserDetails userDetails) {

        commentService.deleteCommentById(commentId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
