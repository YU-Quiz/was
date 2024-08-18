package yuquiz.domain.category.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum CategoryExceptionCode implements ExceptionCode {

    INVALID_ID(404, "존재하지 않는 카테고리입니다.");

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
