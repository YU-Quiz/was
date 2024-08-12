package yuquiz.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "PasswordUpdateReq", description = "비밀번호 업데이트 요청 DTO")
public record PasswordUpdateReq(
        @Schema(description = "현재 비밀번호", example = "password123")
        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        String currentPassword,

        @Schema(description = "새로운 비밀번호", example = "newPassword123")
        @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "비밀번호는 8~16자 영문과 숫자를 사용하세요.")
        String newPassword
) {
}
