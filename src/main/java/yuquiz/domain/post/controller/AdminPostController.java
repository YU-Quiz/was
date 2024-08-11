package yuquiz.domain.post.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.domain.post.service.AdminPostService;

@RestController
@RequestMapping("/api/v1/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping
    public ResponseEntity<?> getLatestPostsByPage(@RequestParam @Min(0) Integer page) {

        Page<PostSummaryRes> posts = adminPostService.getLatestPostsByPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {

        adminPostService.deletePost(postId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
