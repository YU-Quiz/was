package yuquiz.domain.post.dto;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import static yuquiz.domain.post.entity.QPost.post;

public enum PostSortType {
    LIKE_DESC("likeCount", Sort.Direction.DESC, post.likeCount.desc()),
    LIKE_ASC("likeCount", Sort.Direction.ASC, post.likeCount.asc()),
    VIEW_DESC("viewCount", Sort.Direction.DESC, post.viewCount.desc()),
    VIEW_ASC("viewCount", Sort.Direction.ASC, post.viewCount.asc()),
    DATE_DESC("createdAt", Sort.Direction.DESC, post.createdAt.desc()),
    DATE_ASC("createdAt", Sort.Direction.ASC, post.createdAt.asc());

    private String type;
    private Sort.Direction direction;
    @Getter
    private OrderSpecifier<?> order;

    PostSortType(String type, Sort.Direction direction, OrderSpecifier<?> order) {
        this.type = type;
        this.direction = direction;
        this.order = order;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
