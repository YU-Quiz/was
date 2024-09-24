package yuquiz.domain.post.dto;

import yuquiz.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostRes(String title,
                      String content,
                      String categoryName,
                      String nickname,
                      int likeCount,
                      int viewCount,
                      LocalDateTime createdAt,
                      boolean modified,
                      boolean isLiked,
                      boolean isWriter
) {
    public static PostRes fromEntity(Post post, boolean isLiked, boolean isWriter) {

        return new PostRes(
                post.getTitle(),
                post.getContent(),
                post.getCategory().getCategoryName(),
                post.getWriter().getNickname(),
                post.getLikeCount(),
                post.getViewCount(),
                post.getCreatedAt(),
                !post.getModifiedAt().equals(post.getCreatedAt()),
                isLiked,
                isWriter);
    }
}
