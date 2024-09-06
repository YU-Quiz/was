package yuquiz.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.PasswordResetReq;
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

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

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
        PasswordResetReq passwordResetReq = new PasswordResetReq("test", email);
        given(userRepository.existsByUsernameAndEmail(passwordResetReq.username(), passwordResetReq.email()))
                .willReturn(true);

        // when
        boolean isExists = accountService.validateUserForPasswordReset(passwordResetReq);

        // then
        assertTrue(isExists);
    }

    @Test
    @DisplayName("비밀번호 재설정을 위한 확인 테스트 - 불일치")
    void validateUserForPasswordResetFalseTest() {
        // given
        PasswordResetReq passwordResetReq = new PasswordResetReq("test", email);
        given(userRepository.existsByUsernameAndEmail(passwordResetReq.username(), passwordResetReq.email()))
                .willReturn(false);

        // when
        boolean isExists = accountService.validateUserForPasswordReset(passwordResetReq);

        // then
        assertFalse(isExists);
    }
}
