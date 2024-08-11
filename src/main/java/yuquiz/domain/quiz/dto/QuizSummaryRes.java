package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;

import java.time.LocalDateTime;

public record QuizSummaryRes(
        Long quizId,
        String quizTitle,
        String nickname,
        LocalDateTime createdAt,
        Integer likeCount,
        Integer viewCount
) {
    public static QuizSummaryRes fromEntity(Quiz quiz) {
        return new QuizSummaryRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter().getNickname(),
                quiz.getCreatedAt(),
                quiz.getLikeCount(),
                quiz.getViewCount()
        );
    }
}
