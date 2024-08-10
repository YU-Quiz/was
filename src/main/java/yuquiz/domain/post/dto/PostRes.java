package yuquiz.domain.post.dto;

import yuquiz.domain.post.entity.Post;
import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

public record PostRes(
        Long postId,
        String postTitle,
        String nickname,
        LocalDateTime createdAt
) {
    public static PostRes from(Post post) {
        return new PostRes(
                post.getId(),
                post.getTitle(),
                post.getWriter().getNickname(),
                post.getCreatedAt()
        );
    }
}
