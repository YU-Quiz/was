package yuquiz.domain.post.dto;

import yuquiz.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostRes(String title,
                      String content,
                      String category,
                      String nickname,
                      LocalDateTime createdAt,
                      boolean modified
) {
    public static PostRes fromEntity(Post post) {

        return new PostRes(
                post.getTitle(),
                post.getContent(),
                post.getCategory().getCategoryName(),
                post.getWriter().getNickname(),
                post.getCreatedAt(),
                !post.getModifiedAt().equals(post.getCreatedAt()));
    }
}
