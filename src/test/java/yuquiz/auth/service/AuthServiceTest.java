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
import yuquiz.domain.auth.dto.req.OAuthSignUpReq;
import yuquiz.domain.auth.dto.req.SignInReq;
import yuquiz.domain.auth.dto.req.SignUpReq;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.auth.helper.JwtHelper;
import yuquiz.domain.auth.service.AuthService;
import yuquiz.domain.auth.service.OAuthPlatformService;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    private JwtHelper jwtHelper;

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
                .password("password1234")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() {
        // given
        SignUpReq signUpReq = new SignUpReq("test", "password",
                "테스터", "test@naver.com", "컴퓨터공학과", true);

        String encodePassword = passwordEncoder.encode(signUpReq.password());
        User user = signUpReq.toEntity(encodePassword);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        authService.signUp(signUpReq);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("OAuth 회원가입 테스트")
    void oAuthSignUpTest() {
        // given
        OAuthSignUpReq oAuthSignUpReq =
                new OAuthSignUpReq("테스터", "test@naver.com", "컴퓨터공학과", true);

        User user = oAuthSignUpReq.toEntity();
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        authService.oAuthSignUp(oAuthSignUpReq);

        // then
        verify(userRepository, times(1)).save(any(User.class));
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
        when(jwtHelper.createToken(user)).thenReturn(tokenDto);

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
        assertEquals(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD.getStatus(), exception.getStatus());
        assertEquals(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD.getMessage(), exception.getMessage());
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
        assertEquals(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD.getStatus(), exception.getStatus());
        assertEquals(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findByUsername(signInReq.username());
        verify(passwordEncoder, times(1)).matches(signInReq.password(), user.getPassword());
    }

    @Test
    @DisplayName("로그인 테스트 - 계정 잠김")
    void signInFailedByLockedTest() {
        // given
        User lockedUser = User.builder()
                .username(signInReq.username())
                .password("password1234")
                .build();
        lockedUser.updateSuspendStatus(LocalDateTime.now().plusHours(1), 1);

        when(userRepository.findByUsername(signInReq.username())).thenReturn(Optional.of(lockedUser));
        when(passwordEncoder.matches(signInReq.password(), lockedUser.getPassword())).thenReturn(true);


        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signIn(signInReq);
        });

        // then
        assertEquals(UserExceptionCode.USER_LOCKED.getStatus(), exception.getStatus());
        assertTrue(exception.getMessage().contains("잠금 해제 시간"));
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

        doNothing().when(jwtHelper).removeToken(trimAccessToken, refreshToken, response);

        // when
        authService.signOut(accessToken, refreshToken, response);

        // then
        verify(jwtHelper, times(1)).removeToken(trimAccessToken, refreshToken, response);
    }
}
