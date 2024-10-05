package yuquiz.domain.user.dto.res;

import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;

public record UserDetailsRes(
        String username,
        String nickname,
        String email,
        boolean agreeEmail,
        String majorName,
        Role role
) {
    public static UserDetailsRes fromEntity(User user) {
        return new UserDetailsRes(
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.isAgreeEmail(),
                user.getMajorName(),
                user.getRole()
        );
    }
}