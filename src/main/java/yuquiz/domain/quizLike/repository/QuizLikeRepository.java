package yuquiz.domain.quizLike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quizLike.entity.QuizLike;

public interface QuizLikeRepository extends JpaRepository<QuizLike, Long> {
}
