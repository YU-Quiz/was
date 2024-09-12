package yuquiz.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.domain.auth.controller.AuthController;
import yuquiz.domain.auth.dto.req.FindUsernameReq;
import yuquiz.domain.auth.dto.req.OAuthSignUpReq;
import yuquiz.domain.auth.dto.req.PasswordResetReq;
import yuquiz.domain.auth.dto.req.UserVerifyReq;
import yuquiz.domain.auth.dto.req.SignInReq;
import yuquiz.domain.auth.dto.req.SignUpReq;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.auth.service.AccountService;
import yuquiz.domain.auth.service.AuthService;
import yuquiz.domain.auth.service.JwtService;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yuquiz.common.utils.jwt.JwtProperties.ACCESS_HEADER_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private AccountService accountService;

    private TokenDto tokenDto;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        createToken();
    }

    private void createToken() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        tokenDto = TokenDto.of(accessToken, refreshToken);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        // given
        SignUpReq signUpReq = new SignUpReq("test123", "password1234@",
                "테스터", "test@naver.com", "컴퓨터공학과", true);

        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_COOKIE_VALUE, tokenDto.refreshToken())
                .path("/")
                .httpOnly(true)
                .maxAge(60)
                .secure(true)
                .sameSite("None")
                .build();

        given(authService.signUp(any(SignUpReq.class))).willReturn(tokenDto);
        given(cookieUtil.createCookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken())).willReturn(responseCookie);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up")
                        .content(objectMapper.writeValueAsBytes(signUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(tokenDto.accessToken()))
                .andExpect(cookie().value(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()));
    }

    @Test
    @DisplayName("회원가입 테스트 - 유효성 검사 실패")
    void signUpInvalidFailedTest() throws Exception {
        // given
        SignUpReq signUpReq = new SignUpReq(null, null, null, null, null, true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up")
                        .content(objectMapper.writeValueAsBytes(signUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("아이디는 필수 입력입니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 필수 입력 값입니다."))
                .andExpect(jsonPath("$.nickname").value("닉네임은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.email").value("이메일은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.majorName").value("학과는 필수 입력 값입니다."));
    }

    @Test
    @DisplayName("회원가입 테스트 - 조건 불충족")
    void signUpInsufficientTest() throws Exception {
        // given
        SignUpReq signUpReq = new SignUpReq("te", "password", "x",
                "testnaver.com", "컴퓨터공학과", true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up")
                        .content(objectMapper.writeValueAsBytes(signUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("아이디는 영문자와 숫자를 포함하여 6~20자여야 합니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 영문, 숫자, 특수문자를 포함하여 8~16자여야 합니다."))
                .andExpect(jsonPath("$.nickname").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다."))
                .andExpect(jsonPath("$.email").value("유효한 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("회원가입 테스트")
    void oAuthSignUpTest() throws Exception {
        // given
        OAuthSignUpReq oAuthSignUpReq =
                new OAuthSignUpReq("테스터", "test@naver.com", "컴퓨터공학과", true);

        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_COOKIE_VALUE, tokenDto.refreshToken())
                .path("/")
                .httpOnly(true)
                .maxAge(60)
                .secure(true)
                .sameSite("None")
                .build();

        given(authService.oAuthSignUp(any(OAuthSignUpReq.class))).willReturn(tokenDto);
        given(cookieUtil.createCookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken())).willReturn(responseCookie);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up/oauth")
                        .content(objectMapper.writeValueAsBytes(oAuthSignUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(tokenDto.accessToken()))
                .andExpect(cookie().value(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()));
    }

    @Test
    @DisplayName("OAuth 회원가입 테스트 - 유효성 검사 실패")
    void oAuthSignUpInvalidFailedTest() throws Exception {
        // given
        OAuthSignUpReq oAuthSignUpReq =
                new OAuthSignUpReq(null, null, null, true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up/oauth")
                        .content(objectMapper.writeValueAsBytes(oAuthSignUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nickname").value("닉네임은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.email").value("이메일은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.majorName").value("학과는 필수 입력 값입니다."));
    }

    @Test
    @DisplayName("OAuth 회원가입 테스트 - 조건 불충족")
    void oAuthSignUpInsufficientTest() throws Exception {
        // given
        OAuthSignUpReq oAuthSignUpReq =
                new OAuthSignUpReq("x", "testnaver.com", "컴퓨터공학과", true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up/oauth")
                        .content(objectMapper.writeValueAsBytes(oAuthSignUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nickname").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다."))
                .andExpect(jsonPath("$.email").value("유효한 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("로그인 테스트")
    void signInTest() throws Exception {
        // given
        SignInReq signInReq = new SignInReq("test", "password1234");

        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_COOKIE_VALUE, tokenDto.refreshToken())
                .path("/")
                .httpOnly(true)
                .maxAge(60)
                .secure(true)
                .sameSite("None")
                .build();

        given(authService.signIn(any(SignInReq.class))).willReturn(tokenDto);
        given(cookieUtil.createCookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken())).willReturn(responseCookie);

        // then
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-in")
                        .content(objectMapper.writeValueAsBytes(signInReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(tokenDto.accessToken()))
                .andExpect(cookie().value(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()));
    }

    @Test
    @DisplayName("로그인 실패 - 계정 잠김")
    void signInUserLockedTest() throws Exception {
        // given
        SignInReq signInReq = new SignInReq("test", "password1234");

        User lockedUser = User.builder()
                .username(signInReq.username())
                .password("encodedPassword")
                .build();

        lockedUser.updateSuspendStatus(LocalDateTime.now().plusHours(1), 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
        String formattedUnlockedAt = lockedUser.getUnlockedAt().format(formatter);
        String additionalMessage = "잠금 해제 시간: " + formattedUnlockedAt;

        when(authService.signIn(any(SignInReq.class)))
                .thenThrow(new CustomException(UserExceptionCode.USER_LOCKED, additionalMessage));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-in")
                        .content(objectMapper.writeValueAsBytes(signInReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.message").value("정지되어 있는 계정입니다. - " + additionalMessage));
    }

    @Test
    @DisplayName("AccessToken 재발급 테스트")
    void reIssueTokenTest() throws Exception {
        // given
        String reIssueRefreshToken = "reIssueRefreshToken";
        String reIssueAccessToken = "reIssueAccessToken";
        TokenDto newTokenDto = TokenDto.of(reIssueAccessToken, reIssueRefreshToken);

        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_COOKIE_VALUE, newTokenDto.refreshToken())
                .path("/")
                .httpOnly(true)
                .maxAge(60)
                .secure(true)
                .sameSite("None")
                .build();

        given(jwtService.reissueAccessToken(tokenDto.refreshToken())).willReturn(newTokenDto);
        given(cookieUtil.createCookie(REFRESH_COOKIE_VALUE, newTokenDto.refreshToken())).willReturn(responseCookie);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/auth/token-reissue")
                        .cookie(new Cookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newTokenDto.accessToken()))
                .andExpect(cookie().value(REFRESH_COOKIE_VALUE, newTokenDto.refreshToken()));
    }

    @Test
    @DisplayName("AccessToken 재발급 테스트 - Refresh Token 없음")
    void reIssueTokenExceptionTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/auth/token-reissue")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void signOutTest() throws Exception {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        doNothing().when(authService).signOut(eq(accessToken), eq(refreshToken), any(HttpServletResponse.class));

        doAnswer(invocation -> {
            HttpServletResponse resp = invocation.getArgument(1);
            ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_VALUE, "")
                    .path("/")
                    .httpOnly(true)
                    .maxAge(0)
                    .secure(true)
                    .sameSite("None")
                    .build();
            resp.addHeader("Set-Cookie", cookie.toString());
            return null;
        }).when(cookieUtil).deleteCookie(anyString(), any(HttpServletResponse.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/auth/sign-out")
                        .cookie(new Cookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()))
                        .header(ACCESS_HEADER_VALUE, accessToken)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("로그아웃 되었습니다."))
                .andExpect(cookie().doesNotExist(REFRESH_COOKIE_VALUE));;
    }

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findUsernameTest() throws Exception {
        // given
        FindUsernameReq findUsernameReq = new FindUsernameReq("test@gmail.com");
        when(accountService.findUsernameByEmail(findUsernameReq.email())).thenReturn("test");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/find-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(findUsernameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("test"));
    }

    @Test
    @DisplayName("아이디 찾기 실패 테스트 - 유효성 검사 실패")
    void findUsernameFailedTest() throws Exception {
        // given
        FindUsernameReq findUsernameReq = new FindUsernameReq(null);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/find-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(findUsernameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("이메일은 필수 입력 값입니다."));
    }

    @Test
    @DisplayName("아이디 찾기 실패 테스트 - 패턴 불일치")
    void findUsernameInsufficientTest() throws Exception {
        // given
        FindUsernameReq findUsernameReq = new FindUsernameReq("testgmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/find-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(findUsernameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("유효한 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 테스트")
    void verifyUserTest() throws Exception {
        // given
        UserVerifyReq userVerifyReq = new UserVerifyReq("test", "test@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password/verify-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userVerifyReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("메일 전송 완료."));
    }

    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 테스트 - 불일치")
    void verifyUserFailedTest() throws Exception {
        // given
        UserVerifyReq userVerifyReq = new UserVerifyReq("test", "test@gmail.com");
        doThrow(new CustomException(UserExceptionCode.INVALID_USER_INFO)).when(accountService).validateUserForPasswordReset(userVerifyReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password/verify-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userVerifyReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(UserExceptionCode.INVALID_USER_INFO.getStatus()))
                .andExpect(jsonPath("$.message").value(UserExceptionCode.INVALID_USER_INFO.getMessage()));
    }
    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 실패 테스트 - 유효성 검사 실패")
    void verifyUserFailedFailedTest() throws Exception {
        // given
        UserVerifyReq userVerifyReq = new UserVerifyReq(null, null);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password/verify-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userVerifyReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("아이디는 필수 입력입니다."))
                .andExpect(jsonPath("$.email").value("이메일은 필수 입력 값입니다."));
    }

    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 실패 테스트 - 패턴 불일치")
    void verifyUserFailedInsufficientTest() throws Exception {
        // given
        UserVerifyReq userVerifyReq = new UserVerifyReq("test", "testgmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password/verify-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userVerifyReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("유효한 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("비밀번호 재설정 테스트")
    void resetPasswordTest() throws Exception {
        // given
        PasswordResetReq passwordResetReq = new PasswordResetReq("test", "newPassword123@", "code");
        doNothing().when(accountService).resetPassword(passwordResetReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordResetReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("비밀번호 재설정 성공."));
    }

    @Test
    @DisplayName("비밀번호 재설정 실패 테스트 - 유효성 검사 실패")
    void resetPasswordFailedTest() throws Exception {
        // given
        PasswordResetReq passwordResetReq = new PasswordResetReq(null, null, null);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordResetReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("아이디는 필수 입력입니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 필수 입력 값입니다."))
                .andExpect(jsonPath("$.code").value("사용자 code는 필수 입력입니다."));
    }

    @Test
    @DisplayName("비밀번호 재설정 실패 테스트 - 패턴 불일치")
    void resetPasswordInsufficientTest() throws Exception {
        // given
        PasswordResetReq passwordResetReq = new PasswordResetReq("test", "newPassword123", "code");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordResetReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("비밀번호는 영문, 숫자, 특수문자를 포함하여 8~16자여야 합니다."));
    }

/*    @Test
    @DisplayName("로그아웃 테스트 - 쿠키에 Refresh Token 없음")
    void signOutFailedByNotFoundRefreshTokenInCookieTest() throws Exception {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/auth/sign-out")
                        .header(ACCESS_HEADER_VALUE, accessToken)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(JwtExceptionCode.TOKEN_NOT_FOUND.getMessage()));
    }


    @Test
    @DisplayName("로그아웃 테스트 - 헤더에 access Token 없음")
    void signOutFailedByNotFoundAccessTokenInHeaderTest() throws Exception {
        // given
        String refreshToken = "refreshToken";

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/auth/sign-out")
                        .cookie(new Cookie(REFRESH_COOKIE_VALUE, refreshToken))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(JwtExceptionCode.TOKEN_NOT_FOUND.getMessage()));
    }*/

}
