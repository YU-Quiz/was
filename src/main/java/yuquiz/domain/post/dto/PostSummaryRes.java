package yuquiz.domain.post.dto;

import yuquiz.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostSummaryRes(
        Long postId,
        String postTitle,
        String nickname,
        LocalDateTime createdAt
) {
    public static PostSummaryRes fromEntity(Post post) {
        return new PostSummaryRes(
                post.getId(),
                post.getTitle(),
                post.getWriter().getNickname(),
                post.getCreatedAt()
        );
    }
}
