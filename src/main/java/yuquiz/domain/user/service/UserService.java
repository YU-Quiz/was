package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.user.dto.req.PasswordUpdateReq;
import yuquiz.domain.user.dto.req.PasswordReq;
import yuquiz.domain.user.dto.req.SignUpReq;
import yuquiz.domain.user.dto.res.UserDetailsRes;
import yuquiz.domain.user.dto.req.UserUpdateReq;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.entity.Role;
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

    /* OAuth 로그인을 위한 회원 저장 */
    @Transactional
    public User saveOAuthLoginUser(UserInfoDto userInfoDto, OAuthPlatform platform) {

        return userRepository.save(
                User.builder()
                        .username(platform + "_" + userInfoDto.id())
                        .email(userInfoDto.email())
                        .nickname(userInfoDto.nickname())
                        .role(Role.USER)
                        .build()
        );
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

        foundUser.updateUser(updateReq.nickname(),
                updateReq.email(), updateReq.agreeEmail(), updateReq.majorName());
    }

    /* 비밀번호 변경 */
    @Transactional
    public void updatePassword(PasswordUpdateReq passwordReq, Long userId) {

        User foundUser = findUserByUserId(userId);

        if (!checkPassword(passwordReq.currentPassword(), foundUser.getPassword()))
            throw new CustomException(UserExceptionCode.INVALID_PASSWORD);

        String encodePassword = passwordEncoder.encode(passwordReq.newPassword());
        foundUser.updatePassword(encodePassword);
    }

    /* 비밀번호 확인 */
    @Transactional(readOnly = true)
    public boolean verifyPassword(PasswordReq passwordReq, Long userId) {

        String currentPassword = userRepository.findPasswordById(userId);

        return checkPassword(passwordReq.password(), currentPassword);
    }

    /* 사용자 정보 삭제 */
    @Transactional
    public void deleteUserInfo(Long userId) {

        userRepository.deleteById(userId);
    }

    /* 아이디 중복 확인 */
    @Transactional(readOnly = true)
    public boolean verifyUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    /* 닉네임 중복 확인 */
    @Transactional(readOnly = true)
    public boolean verifyNickname(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    /* 사용자 불러오기 */
    private User findUserByUserId(Long userId) {

        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_USERID));
    }

    /* 비밀번호 유효성 검사 */
    private boolean checkPassword(String actual, String expect) {

        return passwordEncoder.matches(actual, expect);
    }
}
