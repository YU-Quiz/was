package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.entity.QuizType;
import yuquiz.domain.triedQuiz.entity.TriedQuiz;

import java.time.LocalDateTime;

public record QuizSummaryRes(
        Long quizId,
        String quizTitle,
        String nickname,
        LocalDateTime createdAt,
        Integer likeCount,
        Integer viewCount,
        Boolean isSolved,
        QuizType quizType
) {
    public static QuizSummaryRes fromEntity(Quiz quiz, Boolean isSolved) {
        return new QuizSummaryRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter().getNickname(),
                quiz.getCreatedAt(),
                quiz.getLikeCount(),
                quiz.getViewCount(),
                isSolved,
                quiz.getQuizType()
        );
    }
}
