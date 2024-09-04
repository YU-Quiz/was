package yuquiz.domain.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.user.entity.User;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q WHERE "
            + "(:keyword IS NULL OR q.title LIKE %:keyword% OR q.question LIKE %:keyword%) "
            + "AND (:subjectId IS NULL OR q.subject.id = :subjectId)")
    Page<Quiz> findQuizzesByKeywordAndSubject(@Param("keyword") String keyword, @Param("subjectId") Long subjectId, Pageable pageable);

    Page<Quiz> findAllByWriter(User writer, Pageable pageable);
}
