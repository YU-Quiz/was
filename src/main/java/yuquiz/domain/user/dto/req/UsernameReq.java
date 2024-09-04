package yuquiz.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "UsernameReq", description = "아이디 중복확인 DTO")
public record UsernameReq(
        @Schema(description = "아이디", example = "test")
        @NotBlank(message = "아이디는 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", message = "아이디는 영문자와 숫자를 포함하여 6~20자여야 합니다.")
        String username
) {
}
