package yuquiz.domain.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.entity.QuizType;
import yuquiz.domain.subject.entity.Subject;
import yuquiz.domain.user.entity.User;

import java.util.List;


public record QuizReq (
    @NotBlank(message = "제목은 필수 입력입니다.")
    String title,

    @NotBlank(message = "질문은 필수 입력입니다.")
    String question,

    List<String> quizImg,

    @NotBlank(message = "정답은 필수 입력입니다.")
    String answer,

    @NotNull(message = "퀴즈 유형은 필수 입력입니다.")
    QuizType quizType,

    List<String> choices,

    @NotNull(message = "과목은 필수 입력입니다.")
    Long subjectId,

    Subject subject,

    User writer
){
    public Quiz toEntity(User writer, Subject subject) {
        return Quiz.builder()
                .title(this.title)
                .question(this.question)
                .quizImgs(this.quizImg)
                .answer(this.answer)
                .quizType(this.quizType)
                .choices(this.choices)
                .writer(writer)
                .subject(subject)
                .build();
    }

}
