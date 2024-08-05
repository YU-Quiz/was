package yuquiz.user.controller;

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
import yuquiz.domain.user.controller.UserController;
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        // given
        SignUpReq signUpReq = SignUpReq.builder()
                .username("test")
                .password("password123")
                .nickname("테스터")
                .email("test@naver.com")
                .majorName("컴퓨터공학과")
                .agreeEmail(true)
                .build();

        doNothing().when(userService).createUser(any(SignUpReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users")
                        .content(objectMapper.writeValueAsBytes(signUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value("회원가입 성공."));
    }

    @Test
    @DisplayName("회원가입 테스트 - 유효성 검사 실패")
    void signUpInvalidFailedTest() throws Exception {
        // given
        SignUpReq signUpReq = SignUpReq.builder().build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users")
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
                .andExpect(jsonPath("$.majorName").value("학과는 필수 선택 값입니다."));
    }

    @Test
    @DisplayName("회원가입 테스트 - 조건 불충족")
    void signUpInsufficientTest() throws Exception {
        // given
        SignUpReq signUpReq = SignUpReq.builder()
                .username("te")
                .password("password")
                .nickname("x")
                .email("testnaver.com")
                .majorName("컴퓨터공학과")
                .agreeEmail(true)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users")
                        .content(objectMapper.writeValueAsBytes(signUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("아이디는 특수문자를 제외한 4~20자리여야 합니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 8~16자 영문과 숫자를 사용하세요."))
                .andExpect(jsonPath("$.nickname").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다."))
                .andExpect(jsonPath("$.email").value("유효한 이메일 형식이 아닙니다."));
    }
}
