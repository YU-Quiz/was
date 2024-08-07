package yuquiz.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.UserRepository;
import yuquiz.domain.user.service.UserService;

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
}
