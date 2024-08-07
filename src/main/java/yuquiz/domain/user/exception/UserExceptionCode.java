package yuquiz.domain.user.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum UserExceptionCode implements ExceptionCode {

    // 로그인 에러
    INVALID_USERNAME_AND_PASSWORD(404, "아이디 또는 비밀번호가 유효하지 않습니다.");

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
