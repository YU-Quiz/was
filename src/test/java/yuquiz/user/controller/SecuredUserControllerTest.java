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
import yuquiz.domain.user.dto.req.PasswordReq;
import yuquiz.domain.user.dto.req.PasswordUpdateReq;
import yuquiz.domain.user.dto.res.UserDetailsRes;
import yuquiz.domain.user.dto.req.UserUpdateReq;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.service.MailCodeService;
import yuquiz.domain.user.service.UserService;
import yuquiz.security.auth.SecurityUserDetails;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class SecuredUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MailCodeService mailCodeService;

    private SecurityUserDetails userDetails;
    private User user;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(get("/**").with(csrf()))
                .defaultRequest(put("/**").with(csrf()))
                .defaultRequest(delete("/**").with(csrf()))
                .defaultRequest(patch("/**").with(csrf()))
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
    @DisplayName("사용자 정보 불러오기 테스트")
    void getUserInfoTest() throws Exception {
        // given
        UserDetailsRes userDetailsRes =
                new UserDetailsRes("test", "테스터", "test@gmail.com", true, "컴퓨터공학과");

        given(userService.getUserInfo(userDetails.getId())).willReturn(userDetailsRes);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/my")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDetailsRes.username()))
                .andExpect(jsonPath("$.nickname").value(userDetailsRes.nickname()))
                .andExpect(jsonPath("$.email").value(userDetailsRes.email()));
    }

    @Test
    @DisplayName("회원 정보 업데이트 테스트")
    void updateUserInfoTest() throws Exception {
        // given
        UserUpdateReq updateReq =
                new UserUpdateReq("테스터1", "new@gmail.com", "컴공", false);

        doNothing().when(userService).updateUserInfo(any(UserUpdateReq.class), eq(userDetails.getId()));

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/users/my")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("회원 정보 수정 성공."));
    }

    @Test
    @DisplayName("회원 업데이트 실패 테스트 - 정보 누락")
    void updateUserInfoFailedTest() throws Exception {
        // given
        UserUpdateReq updateReq =
                new UserUpdateReq(null, null, null, false);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/users/my")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nickname").value("닉네임은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.email").value("이메일은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.majorName").value("학과는 필수 선택 값입니다."));
    }

    @Test
    @DisplayName("회원 업데이트 실패 테스트 - 패턴 불일치")
    void updateUserInfoInsufficientTest() throws Exception {
        // given
        UserUpdateReq updateReq =
                new UserUpdateReq("x", "newgmail.com", "컴공", false);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/users/my")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nickname").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다."))
                .andExpect(jsonPath("$.email").value("유효한 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("회원 정보 삭제 테스트")
    void deleteUserInfoTest() throws Exception {
        // given
        doNothing().when(userService).deleteUserInfo(userDetails.getId());

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/users/my")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 테스트 - 사용자 존재하지 않음")
    void deleteUserInfoFailedByUserNotFoundTest() throws Exception {
        // given
        doThrow(new CustomException(UserExceptionCode.INVALID_USERID)).when(userService).deleteUserInfo(userDetails.getId());

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/users/my")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(UserExceptionCode.INVALID_USERID.getMessage()));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정 테스트")
    void updatePasswordTest() throws Exception {
        // given
        PasswordUpdateReq passwordReq = new PasswordUpdateReq("password123", "newPassword123");

        doNothing().when(userService).updatePassword(any(PasswordUpdateReq.class), eq(userDetails.getId()));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/users/my/password")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("비밀번호 수정 성공."));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정 실패 테스트 - 현재 비밀번호 일치 x")
    void updatePasswordFailedTest() throws Exception {
        // given
        PasswordUpdateReq passwordReq = new PasswordUpdateReq("password123", "newPassword123");

        doThrow(new CustomException(UserExceptionCode.INVALID_PASSWORD))
                .when(userService).updatePassword(any(PasswordUpdateReq.class), eq(userDetails.getId()));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/users/my/password")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(UserExceptionCode.INVALID_PASSWORD.getMessage()));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정 실패 테스트 - 정보 누락")
    void updatePasswordInvalidTest() throws Exception {
        // given
        PasswordUpdateReq passwordReq = new PasswordUpdateReq(null, null);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/users/my/password")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.currentPassword").value("현재 비밀번호를 입력해주세요."))
                .andExpect(jsonPath("$.newPassword").value("새로운 비밀번호를 입력해주세요."));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정 실패 테스트 - 패턴 불일치")
    void updatePasswordInsufficientTest() throws Exception {
        // given
        PasswordUpdateReq passwordReq = new PasswordUpdateReq("password123", "new");

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/users/my/password")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newPassword").value("비밀번호는 8~16자 영문과 숫자를 사용하세요."));
    }

    @Test
    @DisplayName("비밀번호 확인 테스트 - 일치")
    void verifyPasswordTest() throws Exception {
        // given
        PasswordReq passwordReq = new PasswordReq("password123");

        given(userService.verifyPassword(any(PasswordReq.class), eq(userDetails.getId()))).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/my/verify-password")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(true));
    }

    @Test
    @DisplayName("비밀번호 확인 테스트 - 불일치")
    void verifyPasswordInconsistentTest() throws Exception {
        // given
        PasswordReq passwordReq = new PasswordReq("password123");

        given(userService.verifyPassword(any(PasswordReq.class), eq(userDetails.getId()))).willReturn(false);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/users/my/verify-password")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(false));
    }
}
