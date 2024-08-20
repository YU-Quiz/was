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
import yuquiz.domain.post.dto.PostReq;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.dto.PostSortType;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.domain.post.service.PostService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostReq postReq,
                                        @AuthenticationPrincipal SecurityUserDetails userDetails){

        postService.createPost(postReq, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("게시글 생성 성공"));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable(value = "postId") Long postId){

        PostRes postRes = postService.getPostById(postId);

        return ResponseEntity.status(HttpStatus.OK).body(postRes);
    }

    @GetMapping
    public ResponseEntity<?> getPostsByKeyword(@RequestParam(value = "keyword") String keyword,
                                               @RequestParam(value = "sort") PostSortType sort,
                                               @RequestParam(value = "page") @Min(0) Integer page){

        Page<PostSummaryRes> posts = postService.getPostsByKeyword(keyword, sort, page);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getPostsByCategory(@PathVariable(value = "categoryName") String categoryName,
                                                @RequestParam(value = "sort") PostSortType sort,
                                                @RequestParam(value = "page") @Min(0) Integer page){

        Page<PostSummaryRes> posts = postService.getPostsByCategory(categoryName, sort, page);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
