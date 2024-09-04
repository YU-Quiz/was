package yuquiz.domain.notification.dto;

import org.springframework.data.domain.Sort;

public enum NotificationSortType {
    DATE_DESC("createdAt", Sort.Direction.DESC),
    DATE_ASC("createdAt", Sort.Direction.ASC);

    private String type;
    private Sort.Direction direction;

    NotificationSortType(String type, Sort.Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
