package yuquiz.domain.user.dto.res;

import yuquiz.domain.user.entity.User;

public record UserDetailsRes(
        String username,
        String nickname,
        String email,
        boolean agreeEmail,
        String majorName
) {
    public static UserDetailsRes fromEntity(User user) {
        return new UserDetailsRes(
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.isAgreeEmail(),
                user.getMajorName()
        );
    }
}