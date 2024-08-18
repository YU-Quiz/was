package yuquiz.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.post.dto.PostReq;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.service.PostService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostReq postReq, @AuthenticationPrincipal SecurityUserDetails userDetails){

        postService.createPost(postReq, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("게시글 생성 성공"));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                         @PathVariable(value = "postId") Long postId){

        PostRes postRes = postService.getPostById(userDetails.getId(), postId);

        return ResponseEntity.status(HttpStatus.OK).body(postRes);
    }
}
