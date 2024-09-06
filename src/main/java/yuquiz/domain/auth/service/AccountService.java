package yuquiz.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.PasswordResetReq;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;

    /* 아이디 찾기 메서드 */
    public String findUsernameByEmail(String email) {

        User foundUser = userRepository.findByEmail(email).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_EMAIL));

        return foundUser.getUsername();
    }

    /* 비밀번호 찾기를 위한 사용자 확인 메서드 */
    public boolean validateUserForPasswordReset(PasswordResetReq passwordResetReq) {

        return userRepository.existsByUsernameAndEmail(passwordResetReq.username(), passwordResetReq.email());
    }
}