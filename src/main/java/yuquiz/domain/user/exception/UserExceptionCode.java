package yuquiz.domain.user.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum UserExceptionCode implements ExceptionCode {

    // 로그인 에러
    INVALID_USERNAME_AND_PASSWORD(401, "아이디 또는 비밀번호가 유효하지 않습니다."),
    INVALID_PASSWORD(401, "현재 비밀번호와 일치하지 않습니다."),

    INVALID_USERID(404,"존재하지 않는 사용자입니다."),

    EXIST_USERNAME(409, "이미 존재하는 아이디입니다."),
    EXIST_NICKNAME(409, "이미 존재하는 닉네임입니다."),

    USER_LOCKED(423, "정지되어 있는 계정입니다.");

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
