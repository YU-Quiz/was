package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

public record QuizRes(
        Long id,
        String title,
        User writer,
        LocalDateTime createdAt
) {
    public static QuizRes from(Quiz quiz){
        return new QuizRes(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getWriter(),
                quiz.getCreatedAt()
        );
    }
}
