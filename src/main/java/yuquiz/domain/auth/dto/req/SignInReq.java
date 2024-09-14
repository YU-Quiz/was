package yuquiz.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "SignInReq", description = "로그인 DTO")
public record SignInReq(
        @Schema(description = "아이디", example = "test")
        @NotBlank(message = "아이디를 입력해주세요.")
        String username,
        @Schema(description = "비밀번호", example = "password123")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
