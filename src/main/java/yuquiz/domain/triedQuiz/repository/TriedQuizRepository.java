package yuquiz.domain.triedQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.triedQuiz.entity.TriedQuiz;
import yuquiz.domain.user.entity.User;

import java.util.Optional;

public interface TriedQuizRepository extends JpaRepository<TriedQuiz, Long> {
    boolean existsByUserAndQuiz(User user, Quiz quiz);

    @Query("SELECT tq.isSolved FROM TriedQuiz tq WHERE tq.user =:user AND tq.quiz =:quiz")
    Boolean findIsSolvedByUserAndQuiz(@Param("user") User user, @Param("quiz") Quiz quiz);

    Optional<TriedQuiz> findByUserAndQuiz(User user, Quiz quiz);
}
