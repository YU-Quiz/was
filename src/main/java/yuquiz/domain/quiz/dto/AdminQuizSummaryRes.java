package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.triedQuiz.entity.TriedQuiz;

import java.time.LocalDateTime;

public record AdminQuizSummaryRes(
        Long quizId,
        String quizTitle,
        String nickname,
        LocalDateTime createdAt,
        Integer likeCount,
        Integer viewCount
) {
    public static AdminQuizSummaryRes fromEntity(Quiz quiz) {
        return new AdminQuizSummaryRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter().getNickname(),
                quiz.getCreatedAt(),
                quiz.getLikeCount(),
                quiz.getViewCount()
        );
    }
}
