package yuquiz.domain.quiz.dto.quiz;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import static yuquiz.domain.quiz.entity.QQuiz.quiz;

public enum QuizSortType {
    LIKE_DESC("likeCount", Sort.Direction.DESC, quiz.likeCount.desc()),
    LIKE_ASC("likeCount", Sort.Direction.ASC, quiz.likeCount.asc()),
    VIEW_DESC("viewCount", Sort.Direction.DESC, quiz.viewCount.desc()),
    VIEW_ASC("viewCount", Sort.Direction.ASC, quiz.viewCount.asc()),
    DATE_DESC("createdAt", Sort.Direction.DESC, quiz.createdAt.desc()),
    DATE_ASC("createdAt", Sort.Direction.ASC, quiz.createdAt.asc());

    private String type;
    private Sort.Direction direction;

    @Getter
    private OrderSpecifier<?> order;

    QuizSortType(String type, Sort.Direction direction, OrderSpecifier<?> order) {
        this.type = type;
        this.direction = direction;
        this.order = order;
    }

    public Sort getSort() {
        return Sort.by(direction, type);
    }
}
