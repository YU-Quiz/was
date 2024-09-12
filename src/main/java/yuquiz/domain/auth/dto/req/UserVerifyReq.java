package yuquiz.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UserVerifyReq", description = "비밀번호 재설정 전 사용자 확인을 위한 DTO")
public record UserVerifyReq(
        @Schema(description = "아이디", example = "test")
        @NotBlank(message = "아이디는 필수 입력입니다.")
        String username,

        @Schema(description = "아이디", example = "test@gmail.com")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email
) {
}
