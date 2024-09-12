package yuquiz.domain.quiz.dto;

import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.entity.QuizType;

import java.time.LocalDateTime;
import java.util.List;

public record QuizRes(
        String title,

        String question,

        List<String> quizImg,

        QuizType quizType,

        Integer likeCount,

        Integer viewCount,

        List<String> choices,

        String subject,

        String writer,

        LocalDateTime createdAt,

        boolean isLiked,

        boolean isPinned,

        boolean isWriter
) {
    public static QuizRes fromEntity(Quiz quiz, boolean isLiked, boolean isPinned, boolean isWriter) {
        return new QuizRes(
                quiz.getTitle(),
                quiz.getQuestion(),
                quiz.getQuizImgs(),
                quiz.getQuizType(),
                quiz.getLikeCount(),
                quiz.getViewCount(),
                quiz.getChoices(),
                quiz.getSubject().getSubjectName(),
                quiz.getWriter().getNickname(),
                quiz.getCreatedAt(),
                isLiked,
                isPinned,
                isWriter
        );
    }
}
