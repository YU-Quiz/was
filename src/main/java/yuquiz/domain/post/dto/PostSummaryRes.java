package yuquiz.domain.post.dto;

import yuquiz.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostSummaryRes(
        Long postId,
        String postTitle,
        String nickname,
        String categoryName,
        LocalDateTime createdAt,
        int likeCount,
        int viewCount
) {
    public static PostSummaryRes fromEntity(Post post) {
        return new PostSummaryRes(
                post.getId(),
                post.getTitle(),
                post.getWriter().getNickname(),
                post.getCategory().getCategoryName(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getViewCount()
        );
    }
}
