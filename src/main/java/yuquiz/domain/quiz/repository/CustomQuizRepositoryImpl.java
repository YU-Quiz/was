package yuquiz.domain.quiz.repository;

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
    public Page<Quiz> getQuizzes(String keyword, Pageable pageable) {
        List<Quiz> quizzes = jpaQueryFactory
                .selectDistinct(quiz)
                .from(quiz)
                .where(wordContain(keyword))
                .orderBy(quiz.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(quiz.count())
                .from(quiz)
                .where(wordContain(keyword))
                .fetchOne();

        return new PageImpl<>(quizzes, pageable, total);
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

}
