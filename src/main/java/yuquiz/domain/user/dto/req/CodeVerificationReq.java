package yuquiz.domain.user.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CodeVerificationReq(
        @NotBlank(message = "필수 입력 값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "인증번호는 필수 입력 값입니다.")
        String code
) {
}
