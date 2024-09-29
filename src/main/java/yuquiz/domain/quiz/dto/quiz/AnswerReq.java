package yuquiz.domain.quiz.dto.quiz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AnswerReq(
        @Schema(description = "정답", example = "1")
        @NotBlank(message = "정답은 필수 입력입니다.")
        String answer
) {
}
