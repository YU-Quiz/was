package yuquiz.domain.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.entity.QuizType;
import yuquiz.domain.subject.entity.Subject;
import yuquiz.domain.user.entity.User;

import java.util.List;

@Getter
@Setter
public class QuizCreateReq {
    @NotBlank(message = "제목은 필수 입력입니다.")
    String title;

    @NotBlank(message = "질문은 필수 입력입니다.")
    String question;

    List<String> quizImg;

    @NotBlank(message = "정답은 필수 입력입니다.")
    String answer;

    @NotBlank(message = "퀴즈 유형은 필수 입력입니다.")
    QuizType quizType;

    List<String> choices;

    @NotBlank(message = "과목은 필수 입력입니다.")
    Long subjectId;

    Subject subject;

    User writer;

    public Quiz toEntity() {
        return Quiz.builder()
                .title(this.title)
                .question(this.question)
                .quizImgs(this.quizImg)
                .answer(this.answer)
                .quizType(this.quizType)
                .choices(this.choices)
                .writer(this.writer)
                .subject(this.subject)
                .build();
    }
}
