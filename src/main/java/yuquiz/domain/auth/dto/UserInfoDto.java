package yuquiz.domain.auth.dto;

public record UserInfoDto(
        String id,
        String email,
        String nickname
) {
    public static UserInfoDto of(String id, String email, String nickname) {
        return new UserInfoDto(id, email, nickname);
    }
}
