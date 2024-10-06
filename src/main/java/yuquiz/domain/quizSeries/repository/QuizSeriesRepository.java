package yuquiz.domain.quizSeries.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quizSeries.entity.QuizSeries;

public interface QuizSeriesRepository extends JpaRepository<QuizSeries, Long> {
}
