package yuquiz.domain.pinnedQuiz.dto;

import yuquiz.domain.quiz.entity.Quiz;

import java.time.LocalDateTime;

public record PinnedQuizSummaryRes(Long quizId,
                                   String quizTitle,
                                   String nickname,
                                   LocalDateTime createdAt,
                                   Integer likeCount,
                                   Integer viewCount
) {
    public static PinnedQuizSummaryRes fromEntity(Quiz quiz) {
        return new PinnedQuizSummaryRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter().getNickname(),
                quiz.getCreatedAt(),
                quiz.getLikeCount(),
                quiz.getViewCount()
        );
    }
}