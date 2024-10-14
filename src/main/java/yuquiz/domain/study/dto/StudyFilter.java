package yuquiz.domain.study.dto;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;

import java.time.LocalDateTime;

import static yuquiz.domain.study.entity.QStudy.study;

public enum StudyFilter {
    ALL(null),
    ONGOING(study.registerDuration.after(LocalDateTime.now())),
    EXPIRED(study.registerDuration.before(LocalDateTime.now()));

    @Getter
    private final BooleanExpression filter;

    StudyFilter(BooleanExpression filter) {
        this.filter = filter;
    }
}
