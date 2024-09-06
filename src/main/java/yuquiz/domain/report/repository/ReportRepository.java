package yuquiz.domain.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Long countByQuiz(Quiz quiz);
}
