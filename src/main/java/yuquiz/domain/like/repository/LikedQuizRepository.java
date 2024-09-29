package yuquiz.domain.like.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.like.entity.LikedQuiz;
import yuquiz.domain.user.entity.User;

import java.util.Optional;

public interface LikedQuizRepository extends JpaRepository<LikedQuiz, Long> {

    boolean existsByUserAndQuiz(User user, Quiz quiz);

    Page<LikedQuiz> findAllByUser(User user, Pageable pageable);

    Optional<LikedQuiz> findByUserIdAndQuizId(Long userId, Long quizId);
}
