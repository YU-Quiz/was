package yuquiz.domain.pinnedQuiz.dto;

import org.springframework.data.domain.Sort;

public enum PinnedQuizSortType {
    LIKE_DESC("quiz.likeCount", Sort.Direction.DESC),
    LIKE_ASC("quiz.likeCount", Sort.Direction.ASC),
    VIEW_DESC("quiz.viewCount", Sort.Direction.DESC),
    VIEW_ASC("quiz.viewCount", Sort.Direction.ASC),
    LIKED_DATE_DESC("createdAt", Sort.Direction.DESC),
    LIKED_DATE_ASC("createdAt", Sort.Direction.ASC),
    QUIZ_DATE_DESC("quiz.createdAt", Sort.Direction.DESC),
    QUIZ_DATE_ASC("quiz.createdAt", Sort.Direction.ASC);

    private String type;
    private Sort.Direction direction;

    PinnedQuizSortType(String type, Sort.Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
