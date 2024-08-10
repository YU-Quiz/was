package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;

import java.time.LocalDateTime;

public record QuizRes(
        Long id,
        String title,
        String username,
        LocalDateTime createdAt
) {
    public static QuizRes from(Quiz quiz){
        return new QuizRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter().getUsername(),
                quiz.getCreatedAt()
        );
    }
}
