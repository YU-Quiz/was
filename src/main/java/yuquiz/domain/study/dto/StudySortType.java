package yuquiz.domain.study.dto;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;

import static yuquiz.domain.study.entity.QStudy.study;

public enum StudySortType {
    CREATED_DESC(study.createdAt.desc()),
    CREATED_ASC(study.createdAt.asc()),
    REGISTER_DATE_DESC(study.registerDuration.desc()),
    REGISTER_DATE_ASC(study.registerDuration.asc());

    @Getter
    private OrderSpecifier<?> order;

    StudySortType(OrderSpecifier<?> order) {
        this.order = order;
    }
}
