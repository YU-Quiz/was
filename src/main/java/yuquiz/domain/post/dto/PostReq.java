package yuquiz.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import yuquiz.domain.category.entity.Category;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.user.entity.User;

public record PostReq(
        @NotBlank(message = "카테고리는 필수 입력입니다.")
        Long categoryId,

        @NotBlank(message = "제목은 필수 입력입니다.")
        String title,

        @NotBlank(message = "내용은 필수 입력 입니다.")
        String content
) {
    public Post toEntity(User writer, Category category) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .category(category)
                .writer(writer)
                .build();
    }
}
