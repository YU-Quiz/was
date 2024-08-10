package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.entity.QuizType;

import java.util.List;

public record QuizRes(
        String title,

        String question,

        List<String> quizImg,

        QuizType quizType,

        List<String> choices,

        String subject,

        String writer
) {
    public static QuizRes fromEntity(Quiz quiz) {
        return new QuizRes(
                quiz.getTitle(),
                quiz.getQuestion(),
                quiz.getQuizImgs(),
                quiz.getQuizType(),
                quiz.getChoices(),
                quiz.getSubject().getSubjectName(),
                quiz.getWriter().getNickname()
        );
    }
}
