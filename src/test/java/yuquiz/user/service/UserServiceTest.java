package yuquiz.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.user.dto.req.PasswordReq;
import yuquiz.domain.user.dto.req.PasswordUpdateReq;
import yuquiz.domain.user.dto.req.UserUpdateReq;
import yuquiz.domain.user.dto.res.UserDetailsRes;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;
import yuquiz.domain.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Long userId;

    @BeforeEach
    void setUp() {
        this.user = User.builder()
                .username("test")
                .password("password123")
                .email("test@gmail.com")
                .nickname("테스터")
                .agreeEmail(true)
                .majorName("컴퓨터공학과")
                .build();
        this.userId = 1L;
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void getUserInfoTest() {
        // given
        given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));

        // when
        UserDetailsRes userDetailsRes = userService.getUserInfo(userId);

        // then
        verify(userRepository, times(1)).findById(userId);
        assertNotNull(userDetailsRes);
        assertEquals(user.getUsername(), userDetailsRes.username());
        assertEquals(user.getEmail(), userDetailsRes.email());
        assertEquals(user.getNickname(), userDetailsRes.nickname());
    }

    @Test
    @DisplayName("회원 정보 업데이트 테스트")
    void updateUserInfoTest() {
        // given
        UserUpdateReq updateReq =
                new UserUpdateReq("테스터1", "new@gmail.com", "컴공", false);

        given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));

        // when
        userService.updateUserInfo(updateReq, userId);

        // then
        verify(userRepository, times(1)).findById(userId);
        assertEquals(user.getNickname(), updateReq.nickname());
        assertEquals(user.getEmail(), updateReq.email());
        assertEquals(user.getMajorName(), updateReq.majorName());
        assertEquals(user.isAgreeEmail(), updateReq.agreeEmail());
    }

    @Test
    @DisplayName("회원 정보 탈퇴 테스트")
    void deleteUserInfoTest() {
        // given

        // when
        userService.deleteUserInfo(userId);

        // then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("회원 정보 조회 실패 테스트 - 사용자 정보 없음")
    void getUserFailedByUserNotFoundTest() {
        // given
        given(userRepository.findById(userId)).willThrow(new CustomException(UserExceptionCode.INVALID_USERID));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.getUserInfo(userId);
        });

        // then
        assertEquals(UserExceptionCode.INVALID_USERID.getStatus(), exception.getStatus());
        assertEquals(UserExceptionCode.INVALID_USERID.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void updatePasswordTest() {
        // given
        PasswordUpdateReq passwordReq = new PasswordUpdateReq("password123", "newPassword123");

        given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));

        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        // when
        userService.updatePassword(passwordReq, userId);

        // then
        assertEquals(passwordEncoder.encode("newPassword123"), "encodedNewPassword");
    }

    @Test
    @DisplayName("비밀번호 변경 실패 테스트 - 현재 비밀번호 틀림")
    void updatePasswordFailedTest() {
        // given
        PasswordUpdateReq passwordReq = new PasswordUpdateReq("password123", "newPassword123");

        given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));

        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updatePassword(passwordReq, userId);
        });

        // then
        assertEquals(UserExceptionCode.INVALID_PASSWORD.getStatus(), exception.getStatus());
        assertEquals(UserExceptionCode.INVALID_PASSWORD.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("비밀번호 확인 테스트")
    void verifyPasswordTest() {
        // given
        PasswordReq passwordReq = new PasswordReq("password123");

        String encodedPassword = "encodedPassword123";
        given(userRepository.findPasswordById(userId)).willReturn(encodedPassword);

        when(passwordEncoder.matches(passwordReq.password(), encodedPassword)).thenReturn(true);

        // when
        boolean result = userService.verifyPassword(passwordReq, userId);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("아이디 중복 확인 - 존재 o")
    void verifyUsernameExistsTest() {
        // given
        String username = "test";

        given(userRepository.existsByUsername(username)).willReturn(true);

        // when
        boolean result = userService.verifyUsername(username);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("아이디 중복 확인 - 존재 x")
    void verifyUsernameNonExistsTest() {
        // given
        String username = "test";

        given(userRepository.existsByUsername(username)).willReturn(false);

        // when
        boolean result = userService.verifyUsername(username);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 존재 o")
    void verifyNicknameExistsTest() {
        // given
        String nickname = "테스터";

        given(userRepository.existsByNickname(nickname)).willReturn(true);

        // when
        boolean result = userService.verifyNickname(nickname);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 존재 x")
    void verifyNicknameNonExistsTest() {
        // given
        String nickname = "테스터";

        given(userRepository.existsByNickname(nickname)).willReturn(false);

        // when
        boolean result = userService.verifyNickname(nickname);

        // then
        assertFalse(result);
    }
}
