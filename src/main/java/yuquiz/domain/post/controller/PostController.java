package yuquiz.domain.post.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.post.api.PostApi;
import yuquiz.domain.post.dto.PostReq;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.dto.PostSortType;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.domain.post.service.PostService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController implements PostApi {

    private final PostService postService;

    @PostMapping
    @Override
    public ResponseEntity<?> createPost(@Valid @RequestBody PostReq postReq,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails) {

        postService.createPost(postReq, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("게시글 생성 성공"));
    }

    @GetMapping("/{postId}")
    @Override
    public ResponseEntity<?> getPostById(@PathVariable(value = "postId") Long postId,
                                         @AuthenticationPrincipal SecurityUserDetails userDetails) {

        PostRes postRes = postService.getPostById(postId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(postRes);
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getPosts(@RequestParam(value = "keyword", required = false) String keyword,
                                      @RequestParam(value = "categoryId", required = false) Long categoryId,
                                      @RequestParam(value = "sort", defaultValue = "DATE_DESC") PostSortType sort,
                                      @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page) {

        Page<PostSummaryRes> posts = postService.getPosts(keyword, categoryId, sort, page);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @PutMapping("/{postId}")
    @Override
    public ResponseEntity<?> updatePost(@PathVariable(value = "postId") Long postId,
                                        @Valid @RequestBody PostReq postReq,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails) {

        postService.updatePostById(postId, postReq, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("게시글 수정 성공"));
    }

    @DeleteMapping("/{postId}")
    @Override
    public ResponseEntity<?> deletePost(@PathVariable(value = "postId") Long postId,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails) {

        postService.deletePostById(postId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
