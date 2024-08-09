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
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.domain.auth.controller.AuthController;
import yuquiz.domain.auth.dto.SignInReq;
import yuquiz.domain.auth.dto.TokenDto;
import yuquiz.domain.auth.service.AuthService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
    private CookieUtil cookieUtil;

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

        given(authService.reissueAccessToken(tokenDto.refreshToken())).willReturn(newTokenDto);
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
    }
}
