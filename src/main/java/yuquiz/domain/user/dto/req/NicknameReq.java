package yuquiz.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "NicknameReq", description = "닉네임 중복확인 DTO")
public record NicknameReq(
        @Schema(description = "닉네임", example = "테스터")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        String nickname
) {
}
