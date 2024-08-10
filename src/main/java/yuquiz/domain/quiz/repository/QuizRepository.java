package yuquiz.domain.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.quiz.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Page<Quiz> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
