package yuquiz.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "FindUsernameReq", description = "아이디 찾기를 위한 DTO")
public record FindUsernameReq(
        @Schema(description = "아이디", example = "test@gmail.com")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email
) {
}
