package yuquiz.domain.user.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailReq(
        @NotBlank(message = "필수 입력 값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email
) {
}
