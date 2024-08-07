package yuquiz.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInReq(
        @NotBlank(message = "아이디를 입력해주세요.")
        String username,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
