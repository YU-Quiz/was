package yuquiz.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.domain.auth.dto.SignInReq;
import yuquiz.domain.auth.dto.TokenDto;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;
import yuquiz.security.token.refresh.RefreshTokenService;

import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;

    /* 로그인 */
    @Transactional(readOnly = true)
    public TokenDto signIn(SignInReq signInReq) {

        User foundUser = userRepository.findByUsername(signInReq.getUsername()).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD));

        if (!checkPassword(signInReq.getPassword(), foundUser.getPassword())) {
            throw new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD);
        }

        String role = String.valueOf(foundUser.getRole());
        Long userId = foundUser.getId();
        String username = foundUser.getUsername();

        // Jwt Token 생성
        String accessToken = TOKEN_PREFIX + jwtProvider.generateAccessToken(role, userId, username);
        String refreshToken = jwtProvider.generateRefreshToken(role, userId, username);

        refreshTokenService.saveRefreshToken(username, refreshToken);

        return TokenDto.of(accessToken, refreshToken);
    }

    /* 비밀번호 확인 */
    private boolean checkPassword(String actual, String expect) {

        return passwordEncoder.matches(actual, expect);
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
    public void signOut(String refreshToken, HttpServletResponse response) {

        cookieUtil.deleteCookie(REFRESH_COOKIE_VALUE, response);    // 쿠키값 삭제

        refreshTokenService.deleteRefreshToken(refreshToken);       // 로그아웃 시 redis에서 refreshToken 삭제
    }
}
