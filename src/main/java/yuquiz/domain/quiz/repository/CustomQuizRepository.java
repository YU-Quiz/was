package yuquiz.domain.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yuquiz.domain.quiz.dto.quiz.QuizSortType;
import yuquiz.domain.quiz.dto.quiz.QuizSummaryRes;

public interface CustomQuizRepository {
    Page<QuizSummaryRes> getQuizzes(String keyword, Pageable pageable, QuizSortType sort, Long subjectId, Long userId);
}
