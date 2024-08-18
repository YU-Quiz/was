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
import yuquiz.domain.auth.dto.SignInReq;
import yuquiz.domain.auth.dto.TokenDto;
import yuquiz.domain.auth.service.AuthService;
import yuquiz.domain.auth.service.JwtService;
import yuquiz.domain.auth.service.OAuthPlatformService;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OAuthPlatformService oAuthPlatformService;

    @Mock
    private JwtService jwtService;

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
    }

    @Test
    @DisplayName("로그인 테스트")
    void signInTest() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenDto tokenDto = TokenDto.of("Bearer " + accessToken, refreshToken);
        when(userRepository.findByUsername(signInReq.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInReq.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(tokenDto);

        // When
        TokenDto token = authService.signIn(signInReq);

        // Then
        assertNotNull(token);
        assertEquals(TOKEN_PREFIX + accessToken, token.accessToken());
        assertEquals(refreshToken, token.refreshToken());

        verify(userRepository, times(1)).findByUsername(signInReq.username());
        verify(passwordEncoder, times(1)).matches(signInReq.password(), user.getPassword());
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
    @DisplayName("로그아웃 테스트")
    void signOutTest() {
        // given
        String refreshToken = "refreshToken";
        String accessToken = "Bearer accessToken";
        String trimAccessToken = "accessToken";

        doNothing().when(jwtService).deleteAndBlackListToken(trimAccessToken, refreshToken, response);

        // when
        authService.signOut(accessToken, refreshToken, response);

        // then
        verify(jwtService, times(1)).deleteAndBlackListToken(trimAccessToken, refreshToken, response);
    }
}
