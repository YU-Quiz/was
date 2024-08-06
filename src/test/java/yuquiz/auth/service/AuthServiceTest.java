package yuquiz.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import yuquiz.common.exception.CustomException;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.domain.auth.dto.SignInReq;
import yuquiz.domain.auth.dto.TokenDto;
import yuquiz.domain.auth.service.AuthService;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;
import yuquiz.security.token.refresh.RefreshTokenService;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Mock
    private HttpServletResponse response;

    private SignInReq signInReq;
    private User user;

    @BeforeEach
    void setUp() {
        signInReq = new SignInReq("test", "password1234");

        this.user = User.builder()
                .username("test")
                .password(passwordEncoder.encode(signInReq.password()))
                .role(Role.USER)
                .build();
        Long userId = 1L;
        setUserId(user, userId);

    }

    /* User의 id를 임의로 설정 */
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
    @DisplayName("로그인 테스트")
    void signInTest() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(userRepository.findByUsername(signInReq.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInReq.password(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateAccessToken(String.valueOf(user.getRole()), user.getId(), user.getUsername())).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(String.valueOf(user.getRole()), user.getId(), user.getUsername())).thenReturn(refreshToken);

        // When
        TokenDto tokenDto = authService.signIn(signInReq);

        // Then
        assertNotNull(tokenDto);
        assertEquals(TOKEN_PREFIX + accessToken, tokenDto.accessToken());
        assertEquals(refreshToken, tokenDto.refreshToken());

        verify(userRepository, times(1)).findByUsername(signInReq.username());
        verify(passwordEncoder, times(1)).matches(signInReq.password(), user.getPassword());
        verify(jwtProvider, times(1)).generateAccessToken(String.valueOf(user.getRole()), user.getId(), user.getUsername());
        verify(jwtProvider, times(1)).generateRefreshToken(String.valueOf(user.getRole()), user.getId(), user.getUsername());
        verify(refreshTokenService, times(1)).saveRefreshToken(user.getUsername(), refreshToken);
    }

    @Test
    @DisplayName("로그인 테스트 - 아이디 불일치")
    void signInFailedByMismatchUsernameTest() {
        // given
        when(userRepository.findByUsername(signInReq.username()))
                .thenThrow(new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signIn(signInReq);
        });

        // then
        assertEquals(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD, exception.getExceptionCode());
        verify(userRepository, times(1)).findByUsername(signInReq.username());
    }

    @Test
    @DisplayName("로그인 테스트 - 비밀번호 불일치")
    void signInFailedByMismatchPasswordTest() {
        // given
        when(userRepository.findByUsername(signInReq.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInReq.password(), user.getPassword())).thenReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signIn(signInReq);
        });

        // then
        assertEquals(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD, exception.getExceptionCode());
        verify(userRepository, times(1)).findByUsername(signInReq.username());
        verify(passwordEncoder, times(1)).matches(signInReq.password(), user.getPassword());
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
        TokenDto tokenDto = authService.reissueAccessToken(refreshToken);

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

        doNothing().when(cookieUtil).deleteCookie(REFRESH_COOKIE_VALUE, response);
        doNothing().when(refreshTokenService).deleteRefreshToken(refreshToken);

        // when
        authService.signOut(refreshToken, response);

        // then
        verify(cookieUtil, times(1)).deleteCookie(REFRESH_COOKIE_VALUE, response);
        verify(refreshTokenService, times(1)).deleteRefreshToken(refreshToken);
    }
}
