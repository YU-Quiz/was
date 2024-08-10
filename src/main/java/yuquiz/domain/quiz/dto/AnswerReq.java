package yuquiz.domain.quiz.dto;

import jakarta.validation.constraints.NotBlank;

public record AnswerReq(
        @NotBlank(message = "정답은 필수 입력입니다.")
        String answer
) {
}
