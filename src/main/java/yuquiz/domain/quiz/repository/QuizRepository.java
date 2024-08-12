package yuquiz.domain.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.subject.entity.Subject;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  
    Page<Quiz> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Quiz> findAllBySubject(Subject subject, Pageable pageable);

    Page<Quiz> findAllByTitleContainingOrQuestionContaining(String keyword1, String keyword2, Pageable pageable);
}
