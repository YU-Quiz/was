package yuquiz.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "제목", example = "알고리즘 문제")
        @NotBlank(message = "제목은 필수 입력입니다.")
        String title,

        @Schema(description = "질문", example = "퀵정렬 알고리즘의 시간복잡도는?")
        @NotBlank(message = "질문은 필수 입력입니다.")
        String question,

        @Schema(description = "이미지", example = "?")
        List<String> quizImg,

        @Schema(description = "정답", example = "2")
        @NotBlank(message = "정답은 필수 입력입니다.")
        String answer,

        @Schema(description = "유형", example = "MULTIPLE_CHOICE")
        @NotNull(message = "퀴즈 유형은 필수 입력입니다.")
        QuizType quizType,

        @Schema(description = "선지", example = "[\"1.O(N)\",\"2.O(N^2)\",\"3.O(NlogN)\"]")
        List<String> choices,

        @Schema(description = "과목 번호", example = "2")
        @NotNull(message = "과목은 필수 입력입니다.")
        Long subjectId
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
