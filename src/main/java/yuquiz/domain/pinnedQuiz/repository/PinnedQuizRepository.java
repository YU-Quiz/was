package yuquiz.domain.pinnedQuiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.pinnedQuiz.entity.PinnedQuiz;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.user.entity.User;

public interface PinnedQuizRepository extends JpaRepository<PinnedQuiz, Long> {
    void deleteByUserIdAndQuizId(Long userId, Long quizId);

    boolean existsByUserAndQuiz(User user, Quiz quiz);

    Page<PinnedQuiz> findAllByUser(User user, Pageable pageable);
}
