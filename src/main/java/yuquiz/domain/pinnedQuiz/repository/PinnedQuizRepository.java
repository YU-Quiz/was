package yuquiz.domain.pinnedQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.pinnedQuiz.entity.PinnedQuiz;

public interface PinnedQuizRepository extends JpaRepository<PinnedQuiz, Long> {
}
