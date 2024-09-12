package yuquiz.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.PasswordResetReq;
import yuquiz.domain.auth.dto.UserVerifyReq;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordService resetPasswordService;

    /* 아이디 찾기 메서드 */
    @Transactional(readOnly = true)
    public String findUsernameByEmail(String email) {

        User foundUser = userRepository.findByEmail(email).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_EMAIL));

        return foundUser.getUsername();
    }

    /* 비밀번호 찾기를 위한 사용자 확인 메서드 */
    @Transactional(readOnly = true)
    public void validateUserForPasswordReset(UserVerifyReq userVerifyReq) {

        if (!userRepository.existsByUsernameAndEmail(userVerifyReq.username(), userVerifyReq.email())) {
            throw new CustomException(UserExceptionCode.INVALID_USER_INFO);
        }
        resetPasswordService.sendPassResetLinkToMail(userVerifyReq.email(), userVerifyReq.username());
    }

    /* 비밀번호 재설정 */
    @Transactional
    public void resetPassword(PasswordResetReq passwordResetReq) {

        if (!resetPasswordService.isValidCode(passwordResetReq.username(), passwordResetReq.code())) {
            throw new CustomException(UserExceptionCode.UNAUTHORIZED_ACTION);
        }

        String encodePassword = passwordEncoder.encode(passwordResetReq.password());
        userRepository.updatePasswordByUsername(passwordResetReq.username(), encodePassword);
    }
}