package yuquiz.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.user.entity.User;
import yuquiz.security.token.blacklist.BlackListTokenService;
import yuquiz.security.token.refresh.RefreshTokenService;

import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenService refreshTokenService;
    private final BlackListTokenService blackListTokenService;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;

    /* 토큰 발급 */
    public TokenDto generateToken(User user) {

        String role = String.valueOf(user.getRole());
        Long userId = user.getId();
        String username = user.getUsername();

        // Jwt Token 생성
        String accessToken = TOKEN_PREFIX + jwtProvider.generateAccessToken(role, userId, username);
        String refreshToken = jwtProvider.generateRefreshToken(role, userId, username);

        refreshTokenService.saveRefreshToken(username, refreshToken);

        return TokenDto.of(accessToken, refreshToken);
    }

    /* AccessToken 재발급 */
    public TokenDto reissueAccessToken(String refreshTokenInCookie) {

        String refreshToken = refreshTokenService.findRefreshToken(refreshTokenInCookie);
        String reIssueAccessToken = TOKEN_PREFIX + refreshTokenService.accessTokenReIssue(refreshToken);

        // Refresh token rotation(RTR) 사용
        String reIssueRefreshToken = refreshTokenService.refreshTokenReIssue(refreshToken);

        return TokenDto.of(reIssueAccessToken, reIssueRefreshToken);
    }

    /* 로그아웃 */
    public void deleteAndBlackListToken(String accessToken, String refreshToken, HttpServletResponse response) {

        cookieUtil.deleteCookie(REFRESH_COOKIE_VALUE, response);    // 쿠키값 삭제

        refreshTokenService.deleteRefreshToken(refreshToken);       // 로그아웃 시 redis에서 refreshToken 삭제

        blackListTokenService.saveBlackList(accessToken);           // accessToken blackList에 저장
    }
}
