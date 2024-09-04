package yuquiz.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.post.controller.PostController;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.service.PostService;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.security.auth.SecurityUserDetails;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private SecurityUserDetails userDetails;

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

        User user = User.builder()
                .username("test")
                .password("password")
                .nickname("테스터")
                .email("test@gmail.com")
                .role(Role.USER)
                .build();

        this.userDetails = new SecurityUserDetails(user);
    }

    @Test
    @DisplayName("게시글 삭제 - 성공")
    public void deletePostSuccessTest() throws Exception{
        // given
        Long postId = 1L;
        doNothing().when(postService).deletePostById(postId, userDetails.getId());

        // whenr
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/posts/{postId}", postId)
                        .with(user(userDetails)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트 - 권한 없음")
    public void deletePostFailedTest() throws Exception{
        // given
        Long postId = 1L;
        doThrow(new CustomException(PostExceptionCode.UNAUTHORIZED_ACTION)).when(postService).deletePostById(postId, userDetails.getId());

        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/posts/{postId}", postId)
                        .with(user(userDetails)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(PostExceptionCode.UNAUTHORIZED_ACTION.getMessage()));

    }
}
