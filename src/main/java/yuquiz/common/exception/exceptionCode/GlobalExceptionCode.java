package yuquiz.common.exception.exceptionCode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요.");

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
