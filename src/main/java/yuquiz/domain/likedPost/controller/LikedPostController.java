package yuquiz.domain.likedPost.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.likedPost.service.LikedPostService;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class LikedPostController {

    private final LikedPostService likedPostService;

    @GetMapping("/liked")
    public ResponseEntity<?> getLikedPosts(@RequestParam(value = "page") @Min(0) Integer page,
                                           @AuthenticationPrincipal SecurityUserDetails userDetails) {

        Page<PostSummaryRes> likedPosts = likedPostService.getLikedPosts(userDetails.getId(), page);

        return ResponseEntity.status(HttpStatus.OK).body(likedPosts);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable(value = "postId") Long postId,
                                      @AuthenticationPrincipal SecurityUserDetails userDetails) {

        likedPostService.likePost(userDetails.getId(), postId);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("게시글 좋아요 성공"));
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<?> deleteLikePost(@PathVariable(value = "postId") Long postId,
                                            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        likedPostService.deleteLikePost(userDetails.getId(), postId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
