package yuquiz.domain.subject.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum SubjectExceptionCode implements ExceptionCode {
    INVALID_ID(404, "존재하지 않는 과목입니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
