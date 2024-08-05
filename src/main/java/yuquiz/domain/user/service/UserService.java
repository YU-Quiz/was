package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원 생성 */
    @Transactional
    public void createUser(SignUpReq signUpReq) {

        signUpReq.setPassword(passwordEncoder.encode(signUpReq.getPassword()));
        userRepository.save(signUpReq.toEntity());
    }
}
