package yuquiz.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OAuthCodeDto", description = "OAuth에서 발급받은 Code DTO")
public record OAuthCodeDto(
        @Schema(description = "OAuth로 부터 발급받은 Code", example = "oauth_code")
        String code
) {
}
