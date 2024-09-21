package yuquiz.domain.auth.helper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.user.entity.User;
import yuquiz.security.token.blacklist.BlackListTokenService;
import yuquiz.security.token.refresh.RefreshTokenService;

import java.time.LocalDateTime;

import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@RequiredArgsConstructor
@Component
public class JwtHelper {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenService refreshTokenService;
    private final BlackListTokenService blackListTokenService;

    /* access, refresh 토큰 생성 */
    public TokenDto createToken(User user) {

        Long userId = user.getId();
        String username = user.getUsername();
        String role = String.valueOf(user.getRole());

        String accessToken = TOKEN_PREFIX + jwtProvider.generateAccessToken(role, userId, username);
        String refreshToken = jwtProvider.generateRefreshToken(role, userId, username);

        refreshTokenService.saveRefreshToken(username, refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }

    /* access reissue & Refresh Token Rotation */
    public TokenDto reissueToken(String refreshToken) {

        String username = jwtProvider.getUsername(refreshToken);

        if (!refreshTokenService.existedRefreshToken(username))
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);

        Long userId = jwtProvider.getUserId(refreshToken);
        String role = jwtProvider.getRole(refreshToken);

        String newAccessToken = TOKEN_PREFIX + jwtProvider.generateAccessToken(role, userId, username);
        String newRefreshToken = jwtProvider.generateRefreshToken(role, userId, username);

        refreshTokenService.saveRefreshToken(username, newRefreshToken);   // 재발급된 refreshToken redis에 저장.

        return new TokenDto(newAccessToken, newRefreshToken);
    }

    /**
     * 사용자 로그아웃 시,
     * access Token -> BlackList
     * refresh Token -> redis에서 삭제
     */
    public void removeToken(String accessToken, String refreshToken, HttpServletResponse response) {

        deleteAccessToken(accessToken);
        deleteRefreshToken(refreshToken, response);
    }

    private void deleteAccessToken(String accessToken) {

        LocalDateTime accessTokenExpireAt = jwtProvider.getExpiryDate(accessToken);
        blackListTokenService.saveBlackList(accessToken, accessTokenExpireAt);
    }

    private void deleteRefreshToken(String refreshToken, HttpServletResponse response) {

        String username = jwtProvider.getUsername(refreshToken);
        cookieUtil.deleteCookie(REFRESH_COOKIE_VALUE, response);
        refreshTokenService.deleteRefreshToken(username);
    }
}
