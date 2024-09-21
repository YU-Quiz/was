/*
package yuquiz.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.security.token.blacklist.BlackListTokenService;
import yuquiz.security.token.refresh.RefreshTokenService;

import java.lang.reflect.Field;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private BlackListTokenService blackListTokenService;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = User.builder()
                .username("test")
                .role(Role.USER)
                .build();
        Long userId = 1L;
        setUserId(user, userId);

    }

    */
/* User의 id를 임의로 설정 *//*

    private void setUserId(User user, Long id) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void signInTest() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        when(jwtProvider.generateAccessToken(String.valueOf(user.getRole()), user.getId(), user.getUsername())).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(String.valueOf(user.getRole()), user.getId(), user.getUsername())).thenReturn(refreshToken);

        // When
        TokenDto tokenDto = jwtService.generateToken(user);

        // Then
        assertNotNull(tokenDto);
        assertEquals(TOKEN_PREFIX + accessToken, tokenDto.accessToken());
        assertEquals(refreshToken, tokenDto.refreshToken());
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    void reissueAccessTokenTest() {
        // given
        String refreshToken = "refreshToken";
        String reIssueAccessToken = "reIssueAccessToken";
        String reIssueRefreshToken = "reIssueRefreshToken";

        when(refreshTokenService.findRefreshToken(refreshToken)).thenReturn(refreshToken);
        when(refreshTokenService.accessTokenReIssue(refreshToken)).thenReturn(reIssueAccessToken);
        when(refreshTokenService.refreshTokenReIssue(refreshToken)).thenReturn(reIssueRefreshToken);

        // when
        TokenDto tokenDto = jwtService.reissueAccessToken(refreshToken);

        // then
        assertNotNull(tokenDto);
        assertEquals(TOKEN_PREFIX + reIssueAccessToken, tokenDto.accessToken());
        assertEquals(reIssueRefreshToken, tokenDto.refreshToken());

        verify(refreshTokenService, times(1)).findRefreshToken(refreshToken);
        verify(refreshTokenService, times(1)).accessTokenReIssue(refreshToken);
        verify(refreshTokenService, times(1)).refreshTokenReIssue(refreshToken);
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void signOutTest() {
        // given
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";

        doNothing().when(cookieUtil).deleteCookie(REFRESH_COOKIE_VALUE, response);
        doNothing().when(refreshTokenService).deleteRefreshToken(refreshToken);
        doNothing().when(blackListTokenService).saveBlackList(accessToken);

        // when
        jwtService.deleteAndBlackListToken(accessToken, refreshToken, response);

        // then
        verify(cookieUtil, times(1)).deleteCookie(REFRESH_COOKIE_VALUE, response);
        verify(refreshTokenService, times(1)).deleteRefreshToken(refreshToken);
        verify(blackListTokenService, times(1)).saveBlackList(accessToken);
    }
}
*/
