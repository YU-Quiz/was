package yuquiz.domain.quizLike.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quizLike.entity.QuizLike;
import yuquiz.domain.user.entity.User;

public interface QuizLikeRepository extends JpaRepository<QuizLike, Long> {
    void deleteByUserAndQuiz(User user, Quiz quiz);

    boolean existsByUserAndQuiz(User user, Quiz quiz);

    Page<QuizLike> findAllByUser(User user, Pageable pageable);
}
