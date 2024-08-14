package yuquiz.domain.auth.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {

    UNSUPPORTED_SOCIAL_LOGIN_ERROR(400, "지원하지 않는 소셜 로그인입니다."),
    JSON_PROCESSING_ERROR(500, "JSON 처리 중 에러가 발생했습니다.");

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
