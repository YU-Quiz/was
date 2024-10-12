package yuquiz.domain.quiz.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import yuquiz.domain.quiz.entity.TriedQuiz;

import java.util.List;

import static yuquiz.domain.quiz.entity.QTriedQuiz.triedQuiz;

public class CustomTriedQuizRepositoryImpl implements CustomTriedQuizRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomTriedQuizRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<TriedQuiz> getTriedQuizzes(Long userId){

        return jpaQueryFactory
                .select(triedQuiz)
                .from(triedQuiz)
                .where(triedQuiz.user.id.eq(userId))
                .fetch();
    }
}
