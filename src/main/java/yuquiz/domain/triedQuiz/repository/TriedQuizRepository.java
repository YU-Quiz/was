package yuquiz.domain.triedQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.triedQuiz.entity.TriedQuiz;

public interface TriedQuizRepository extends JpaRepository<TriedQuiz, Long> {
}
