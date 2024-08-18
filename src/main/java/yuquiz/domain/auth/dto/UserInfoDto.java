package yuquiz.domain.auth.dto;

public record UserInfoDto(
        String id
) {
    public static UserInfoDto of(String id) {
        return new UserInfoDto(id);
    }
}
