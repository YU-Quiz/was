package yuquiz.common.exception.exceptionCode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JwtExceptionCode implements ExceptionCode{

    ACCESS_TOKEN_EXPIRED(401, "Access Token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(401, "Access Token이 잘못되었습니다."),
    BLACKLIST_ACCESS_TOKEN(401, "접근 불가한 AccessToken입니다."),

    REFRESH_TOKEN_NOT_FOUND(404, "RefreshToken이 존재하지 않습니다."),
    TOKEN_NOT_FOUND(404, "Token이 존재하지 않습니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
