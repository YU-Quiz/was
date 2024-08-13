package yuquiz.domain.user.dto;

import org.springframework.data.domain.Sort;

public enum UserSortType {
    NICK_DESC("nickname", Sort.Direction.DESC),
    NICK_ASC("nickname", Sort.Direction.ASC),
    MAIL_DESC("email", Sort.Direction.DESC),
    MAIL_ASC("email", Sort.Direction.ASC),
    BAN_DESC("bannedCnt", Sort.Direction.DESC),
    BAN_ASC("bannedCnt", Sort.Direction.ASC),
    ROLE_DESC("role", Sort.Direction.DESC),
    ROLE_ASC("role", Sort.Direction.ASC),
    DATE_DESC("createdAt", Sort.Direction.DESC),
    DATE_ASC("createdAt", Sort.Direction.ASC);

    private String type;
    private Sort.Direction direction;

    UserSortType(String type, Sort.Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
