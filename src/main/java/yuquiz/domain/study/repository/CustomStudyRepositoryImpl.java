package yuquiz.domain.study.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import yuquiz.domain.study.dto.StudyFilter;
import yuquiz.domain.study.dto.StudySortType;
import yuquiz.domain.study.entity.Study;

import java.util.List;

import static yuquiz.domain.study.entity.QStudy.study;

public class CustomStudyRepositoryImpl implements CustomStudyRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomStudyRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Study> getStudies(String keyword, Pageable pageable, StudySortType sort, StudyFilter filter) {
        List<Study> studies = jpaQueryFactory
                .select(study)
                .from(study)
                .where(wordContain(keyword), filter.getFilter())
                .orderBy(sort.getOrder())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(study.count())
                .from(study)
                .where(wordContain(keyword))
                .fetchOne();

        return new PageImpl<>(studies, pageable, total);
    }

    private BooleanExpression wordContain(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return Expressions.booleanTemplate(
                "function('match', {0}, {1}, {2}) > 0",
                study.studyName,
                study.description,
                keyword
        );
    }
}
