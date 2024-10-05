package yuquiz.domain.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yuquiz.domain.quiz.entity.Quiz;

public interface CustomQuizRepository {
    Page<Quiz> getQuizzes(String keyword, Pageable pageable, Long subjectId);
}
