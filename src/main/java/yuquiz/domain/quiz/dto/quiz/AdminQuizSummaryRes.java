package yuquiz.domain.quiz.dto.quiz;

import yuquiz.domain.quiz.entity.Quiz;

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
