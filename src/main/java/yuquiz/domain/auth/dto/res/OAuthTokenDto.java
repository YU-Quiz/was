package yuquiz.domain.auth.dto.res;

public record OAuthTokenDto(
        boolean isRegistered,
        TokenDto tokenDto
) {
    public static OAuthTokenDto of(boolean isRegistered, TokenDto tokenDto) {
        return new OAuthTokenDto(isRegistered, tokenDto);
    }
}