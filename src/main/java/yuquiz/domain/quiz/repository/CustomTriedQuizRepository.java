package yuquiz.domain.quiz.repository;

import yuquiz.domain.quiz.entity.TriedQuiz;

import java.util.List;

public interface CustomTriedQuizRepository {
    List<TriedQuiz> getTriedQuizzes(Long userId);
}
