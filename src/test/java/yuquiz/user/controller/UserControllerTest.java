package yuquiz.user.controller;

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
import yuquiz.domain.user.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
        String username = "test";

        given(userService.verifyUsername(username)).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/verify-username")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON)
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
        String username = "test";

        given(userService.verifyUsername(username)).willReturn(false);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/verify-username")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON)
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
        String nickname = "test";

        given(userService.verifyNickname(nickname)).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/verify-nickname")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON)
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
        String nickname = "test";

        given(userService.verifyNickname(nickname)).willReturn(false);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/verify-nickname")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }
}
