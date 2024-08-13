package yuquiz.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "PasswordReq", description = "비밀번호 확인 DTO")
public record PasswordReq(
        @Schema(description = "현재 비밀번호", example = "password123")
        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        String password
) {
}
