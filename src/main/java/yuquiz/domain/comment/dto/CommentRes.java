package yuquiz.domain.comment.dto;

import yuquiz.domain.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentRes(
        Long id,
        String content,
        String writerName,
        LocalDateTime createdAt,
        boolean modified,
        boolean isWriter
) {
    public static CommentRes fromEntity(Comment comment, boolean isWriter) {
        return new CommentRes(
                comment.getId(),
                comment.getContent(),
                comment.getWriter().getNickname(),
                comment.getCreatedAt(),
                !comment.getModifiedAt().equals(comment.getCreatedAt()),
                isWriter
        );
    }
}
