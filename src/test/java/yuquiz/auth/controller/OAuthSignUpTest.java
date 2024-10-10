package yuquiz.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.domain.auth.controller.AuthController;
import yuquiz.domain.auth.dto.req.OAuthSignUpReq;
import yuquiz.domain.auth.service.AccountService;
import yuquiz.domain.auth.service.AuthService;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.security.auth.SecurityUserDetails;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class OAuthSignUpTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private AccountService accountService;

    private SecurityUserDetails userDetails;
    private User user;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/**").with(csrf()))
                .build();

        this.user = User.builder()
                .username("test")
                .password("password")
                .nickname("테스터")
                .email("test@gmail.com")
                .role(Role.USER)
                .build();

        this.userDetails = new SecurityUserDetails(user);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void oAuthSignUpTest() throws Exception {
        // given
        OAuthSignUpReq oAuthSignUpReq =
                new OAuthSignUpReq("테스터", "test@naver.com", "컴퓨터공학과", true);

        Long userId = 1L;
        doNothing().when(authService).oAuthSignUp(any(OAuthSignUpReq.class), eq(userId));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/sign-up/oauth")
                        .with(user(userDetails))
                        .content(objectMapper.writeValueAsBytes(oAuthSignUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("회원가입 성공"));
    }

}
