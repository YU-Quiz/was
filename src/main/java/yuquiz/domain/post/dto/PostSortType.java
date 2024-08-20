package yuquiz.domain.post.dto;

import org.springframework.data.domain.Sort;

public enum PostSortType {
    LIKE_DESC("likeCount", Sort.Direction.DESC),
    LIKE_ASC("likeCount", Sort.Direction.ASC),
    VIEW_DESC("viewCount", Sort.Direction.DESC),
    VIEW_ASC("viewCount", Sort.Direction.ASC),
    DATE_DESC("createdAt", Sort.Direction.DESC),
    DATE_ASC("createdAt", Sort.Direction.ASC);

    private String type;
    private Sort.Direction direction;

    PostSortType(String type, Sort.Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
