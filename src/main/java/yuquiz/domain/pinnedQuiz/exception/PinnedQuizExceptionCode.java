package yuquiz.domain.pinnedQuiz.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum PinnedQuizExceptionCode implements ExceptionCode {

    ALREADY_PINNED(409, "이미 즐겨찾기된 퀴즈입니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
