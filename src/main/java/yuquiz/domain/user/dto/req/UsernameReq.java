package yuquiz.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "UsernameReq", description = "아이디 중복확인 DTO")
public record UsernameReq(
        @Schema(description = "아이디", example = "test")
        @NotBlank(message = "아이디는 필수 입력입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9-_]{4,20}$", message = "아이디는 특수문자와 한글을 제외한 4~20자리여야 합니다.")
        String username
) {
}
