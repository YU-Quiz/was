package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.dto.UserDetailsRes;
import yuquiz.domain.user.dto.UserUpdateReq;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원 생성 */
    @Transactional
    public void createUser(SignUpReq signUpReq) {

        String encodePassword = passwordEncoder.encode(signUpReq.password());
        userRepository.save(signUpReq.toEntity(encodePassword));
    }

    /* 사용자 정보 불러오기 */
    @Transactional(readOnly = true)
    public UserDetailsRes getUserInfo(Long userId) {

        return UserDetailsRes.fromEntity(findUserByUserId(userId));
    }

    /* 사용자 정보 업데이트 */
    @Transactional
    public void updateUserInfo(UserUpdateReq updateReq, Long userId) {

        User foundUser = findUserByUserId(userId);

        String encodePassword = passwordEncoder.encode(updateReq.password());

        foundUser.updateUser(encodePassword, updateReq.nickname(),
                updateReq.email(), updateReq.agreeEmail(), updateReq.majorName());
    }

    /* 사용자 정보 삭제 */
    @Transactional
    public void deleteUserInfo(Long userId) {

        userRepository.deleteById(userId);
    }

    /* 사용자 불러오기 */
    private User findUserByUserId(Long userId) {

        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_USERID));
    }

}
