package yuquiz.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import yuquiz.domain.user.entity.User;

@Getter @Setter
@Builder
public class SignUpDto {

    private String username;
    private String password;
    private String nickname;
    private String email;
    private String majorName;
    private boolean agreeEmail;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .majorName(majorName)
                .agreeEmail(agreeEmail)
                .build();
    }
}
