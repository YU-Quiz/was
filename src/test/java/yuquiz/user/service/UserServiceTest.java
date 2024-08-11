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
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.dto.UserDetailsRes;
import yuquiz.domain.user.dto.UserUpdateReq;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;
import yuquiz.domain.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void SetUp() {
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
    @DisplayName("회원가입 테스트")
    void signUpTest() {
        // given
        SignUpReq signUpReq = new SignUpReq("test", "password",
                        "테스터", "test@naver.com", "컴퓨터공학과", true);

        String encodePassword = passwordEncoder.encode(signUpReq.password());
        User user = signUpReq.toEntity(encodePassword);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        userService.createUser(signUpReq);

        // then
        verify(userRepository, times(1)).save(any(User.class));
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
        assertEquals(userDetailsRes.username(), user.getUsername());
        assertEquals(userDetailsRes.email(), user.getEmail());
        assertEquals(userDetailsRes.nickname(), user.getNickname());
    }

    @Test
    @DisplayName("회원 정보 업데이트 테스트")
    void updateUserInfoTest() {
        // given
        UserUpdateReq updateReq =
                new UserUpdateReq("newPassword1234", "테스터1", "new@gmail.com", "컴공", false);

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
        assertEquals(UserExceptionCode.INVALID_USERID, exception.getExceptionCode());
        verify(userRepository, times(1)).findById(userId);
    }
}
