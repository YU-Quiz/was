package yuquiz.domain.auth.dto.res;

public record UserInfoDto(
        String id
) {
    public static UserInfoDto of(String id) {
        return new UserInfoDto(id);
    }
}
