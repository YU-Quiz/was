package yuquiz.domain.quiz.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import yuquiz.domain.quiz.entity.Quiz;

import java.util.List;

import static yuquiz.domain.quiz.entity.QQuiz.quiz;


public class CustomQuizRepositoryImpl implements CustomQuizRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomQuizRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Quiz> getQuizzes(String keyword, Pageable pageable, String sort, Long subjectId) {
        List<Quiz> quizzes = jpaQueryFactory
                .selectDistinct(quiz)
                .from(quiz)
                .where(wordContain(keyword), subjectEqual(subjectId))
                .orderBy(getSort(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(quiz.count())
                .from(quiz)
                .where(wordContain(keyword), subjectEqual(subjectId))
                .fetchOne();

        return new PageImpl<>(quizzes, pageable, total);
    }

    private OrderSpecifier<?> getSort(String sort) {
        switch (sort) {
            case "LIKE_DESC":
                return quiz.likeCount.desc();
            case "LIKE_ASC":
                return quiz.likeCount.asc();
            case "VIEW_DESC":
                return quiz.viewCount.desc();
            case "VIEW_ASC":
                return quiz.viewCount.asc();
            case "DATE_ASC":
                return quiz.createdAt.asc();
            default:
                return quiz.createdAt.desc();
        }
    }

    private BooleanExpression wordContain(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return Expressions.booleanTemplate(
                "function('match', {0}, {1}, {2}) > 0",
                quiz.title,
                quiz.question,
                keyword
        );
    }

    private BooleanExpression subjectEqual(Long subjectId) {
        if (subjectId == null) {
            return null;
        }
        return quiz.subject.id.eq(subjectId);
    }

}
