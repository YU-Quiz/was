package yuquiz.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "PasswordResetReq", description = "비밀번호 재설정을 위한 DTO")
public record PasswordResetReq(
        @Schema(description = "아이디", example = "test")
        @NotBlank(message = "아이디는 필수 입력입니다.")
        String username,

        @Schema(description = "새로운 비밀번호", example = "newPassword123@")
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~16자여야 합니다.")
        String password,

        @Schema(description = "사용자에게 발급한 uuid", example = "uuid code")
        @NotBlank(message = "사용자 code는 필수 입력입니다.")
        String code
) {
}
