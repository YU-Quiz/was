package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;

import java.time.LocalDateTime;

public record QuizRes(
        Long quizId,
        String quizTitle,
        String nickname,
        LocalDateTime createdAt
) {
    public static QuizRes fromEntity(Quiz quiz){
        return new QuizRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter().getNickname(),
                quiz.getCreatedAt()
        );
    }
}
