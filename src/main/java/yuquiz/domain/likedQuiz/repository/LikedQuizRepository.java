package yuquiz.domain.likedQuiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.likedQuiz.entity.LikedQuiz;
import yuquiz.domain.user.entity.User;

public interface LikedQuizRepository extends JpaRepository<LikedQuiz, Long> {
    void deleteByUserAndQuiz(User user, Quiz quiz);

    boolean existsByUserAndQuiz(User user, Quiz quiz);

    Page<LikedQuiz> findAllByUser(User user, Pageable pageable);
}
