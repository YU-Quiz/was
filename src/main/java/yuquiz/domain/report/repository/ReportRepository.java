package yuquiz.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Long countByQuiz(Quiz quiz);

    @Modifying
    @Query("delete from Report r where r.quiz.id = :quizId")
    void deleteByQuiz(@Param("quizId") Long quizId);
}
