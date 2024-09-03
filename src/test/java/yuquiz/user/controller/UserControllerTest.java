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
import yuquiz.common.exception.CustomException;
import yuquiz.domain.user.controller.UserController;
import yuquiz.domain.user.dto.req.CodeVerificationReq;
import yuquiz.domain.user.dto.req.EmailReq;
import yuquiz.domain.user.dto.req.NicknameReq;
import yuquiz.domain.user.dto.req.UsernameReq;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.service.MailCodeService;
import yuquiz.domain.user.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    @MockBean
    private MailCodeService mailCodeService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 존재 o")
    void verifyUsernameExistsTest() throws Exception {
        // given
        UsernameReq usernameReq = new UsernameReq("test");

        given(userService.verifyUsername(usernameReq.username())).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/verify-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(usernameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다."));
    }

    @Test
    @DisplayName("아이디 중복 확인 - 존재 x")
    void verifyUsernameNonExistsTest() throws Exception {
        // given
        UsernameReq usernameReq = new UsernameReq("test");

        given(userService.verifyUsername(usernameReq.username())).willReturn(false);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/verify-username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(usernameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 존재 o")
    void verifyNicknameExistsTest() throws Exception {
        // given
        NicknameReq nicknameReq = new NicknameReq("test");

        given(userService.verifyNickname(nicknameReq.nickname())).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/verify-nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(nicknameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 닉네임입니다."));
    }

    @Test
    @DisplayName("아이디 중복 확인 - 존재 x")
    void verifyNicknameNonExistsTest() throws Exception {
        // given
        NicknameReq nicknameReq = new NicknameReq("test");

        given(userService.verifyNickname(nicknameReq.nickname())).willReturn(false);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/verify-nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(nicknameReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일로 인증 코드 보내기 - 성공")
    void sendCodeToMailSuccessTest() throws Exception {
        // given
        EmailReq emailReq = new EmailReq("test@gmail.com");
        doNothing().when(mailCodeService).sendCodeToMail(emailReq.email());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/email/verification-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(emailReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("인증메일 보내기 성공."));
    }

    @Test
    @DisplayName("이메일로 인증 코드 보내기 - 실패 (1분 이내 재전송 시도)")
    void sendCodeToMailAlreadyRequestedTest() throws Exception {
        // given
        EmailReq emailReq = new EmailReq("test@gmail.com");
        doThrow(new CustomException(UserExceptionCode.ALREADY_MAIL_REQUEST)).when(mailCodeService).sendCodeToMail(emailReq.email());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/email/verification-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(emailReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value(UserExceptionCode.ALREADY_MAIL_REQUEST.getMessage()));
    }


    @Test
    @DisplayName("인증 코드 확인 - 성공")
    void verifyCodeSuccessTest() throws Exception {
        // given
        CodeVerificationReq codeReq = new CodeVerificationReq("test@gmail.com", "123456");
        given(mailCodeService.verifiedCode(codeReq)).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/email/code-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(codeReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(true));
    }

    @Test
    @DisplayName("인증 코드 확인 - 실패 (이미 존재하는 이메일)")
    void verifyCodeEmailExistsTest() throws Exception {
        // given
        CodeVerificationReq codeReq = new CodeVerificationReq("test@gmail.com", "123456");
        doThrow(new CustomException(UserExceptionCode.EXIST_EMAIL)).when(mailCodeService).verifiedCode(codeReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/email/code-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(codeReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(UserExceptionCode.EXIST_EMAIL.getMessage()));
    }


    @Test
    @DisplayName("인증 코드 확인 - 실패 (코드 불일치)")
    void verifyCodeMismatchTest() throws Exception {
        // given
        CodeVerificationReq codeReq = new CodeVerificationReq("test@gmail.com", "123456");
        doThrow(new CustomException(UserExceptionCode.INVALID_CODE)).when(mailCodeService).verifiedCode(codeReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/email/code-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(codeReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(UserExceptionCode.INVALID_CODE.getMessage()));
    }

    @Test
    @DisplayName("인증 코드 확인 - 실패 (코드 만료)")
    void verifyCodeExpiredTest() throws Exception {
        // given
        CodeVerificationReq codeReq = new CodeVerificationReq("test@gmail.com", "123456");
        doThrow(new CustomException(UserExceptionCode.CODE_EXPIRED)).when(mailCodeService).verifiedCode(codeReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/email/code-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(codeReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.message").value(UserExceptionCode.CODE_EXPIRED.getMessage()));
    }
}
