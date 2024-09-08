package yuquiz.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import yuquiz.domain.comment.entity.Comment;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.user.entity.User;

public record CommentReq(
        @NotBlank(message = "내용은 필수 입력입니다.")
        String content
) {

    public Comment toEntity(User writer, Post post) {
        return Comment.builder()
                .content(this.content)
                .writer(writer)
                .post(post)
                .build();
    }
}
