package yuquiz.domain.subject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.subject.entity.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s WHERE "
            + "(:keyword IS NULL OR :keyword = '' OR s.subjectName LIKE %:keyword%) "
            + "OR (:keyword IS NULL OR :keyword = '' OR s.subjectCode = :keyword)")
    List<Subject> findSubjectsByKeyword(@Param("keyword") String keyword);
}
