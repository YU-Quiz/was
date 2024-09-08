package yuquiz.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.PasswordResetReq;
import yuquiz.domain.auth.dto.UserVerifyReq;
import yuquiz.domain.auth.service.AccountService;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    private String email;
    private User user;

    @BeforeEach
    void setUp() {
        this.email = "test@gmail.com";
        this.user = User.builder()
                .username("test")
                .password("password1234@")
                .email(email)
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("사용자 아이디 찾기 테스트")
    void findUsernameByEmailTest() {
        // given
        given(userRepository.findByEmail(email)).willReturn(Optional.ofNullable(user));

        // when
        String username = accountService.findUsernameByEmail(email);

        // then
        assertNotNull(username);
        assertEquals("test", username);
    }

    @Test
    @DisplayName("사용자 아이디 찾기 실패 테스트 - 존재하지 않는 사용자")
    void findUsernameBuEmailFailedByUserNotFound() {
        // given
        given(userRepository.findByEmail(email)).willThrow(new CustomException(UserExceptionCode.INVALID_EMAIL));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            accountService.findUsernameByEmail(email);
        });

        // then
        assertEquals(UserExceptionCode.INVALID_EMAIL.getStatus(), exception.getStatus());
        assertEquals(UserExceptionCode.INVALID_EMAIL.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 테스트 - 일치")
    void validateUserForPasswordResetTrueTest() {
        // given
        UserVerifyReq userVerifyReq = new UserVerifyReq("test", email);
        given(userRepository.existsByUsernameAndEmail(userVerifyReq.username(), userVerifyReq.email()))
                .willReturn(true);

        // when
        boolean isExists = accountService.validateUserForPasswordReset(userVerifyReq);

        // then
        assertTrue(isExists);
    }

    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 테스트 - 불일치")
    void validateUserForPasswordResetFalseTest() {
        // given
        UserVerifyReq userVerifyReq = new UserVerifyReq("test", email);
        given(userRepository.existsByUsernameAndEmail(userVerifyReq.username(), userVerifyReq.email()))
                .willReturn(false);

        // when
        boolean isExists = accountService.validateUserForPasswordReset(userVerifyReq);

        // then
        assertFalse(isExists);
    }

    @Test
    @DisplayName("비밀번호 재설정 테스트")
    void resetPasswordTest() {
        // given
        PasswordResetReq passwordResetReq = new PasswordResetReq("test", "newPassword123@@");
        when(passwordEncoder.encode("newPassword123@@")).thenReturn("encodedNewPassword");
        doNothing().when(userRepository).updatePasswordByUsername("test", "encodedNewPassword");

        // when
        accountService.resetPassword(passwordResetReq);

        // then
        verify(userRepository).updatePasswordByUsername(passwordResetReq.username(), "encodedNewPassword");
    }
}
