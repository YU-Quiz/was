package yuquiz.domain.comment.dto;

import yuquiz.domain.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentRes(
        Long id,
        String content,
        String writerName,
        LocalDateTime createdAt,
        boolean modified
) {
    public static CommentRes fromEntity(Comment comment) {
        return new CommentRes(
                comment.getId(),
                comment.getContent(),
                comment.getWriter().getNickname(),
                comment.getCreatedAt(),
                !comment.getModifiedAt().equals(comment.getCreatedAt())
        );
    }
}
