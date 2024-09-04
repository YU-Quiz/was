package yuquiz.domain.quiz.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum QuizExceptionCode implements ExceptionCode {

    INVALID_ID(404, "존재하지 않는 퀴즈입니다."),
    ALREADY_PINNED(409, "이미 즐겨찾기 한 퀴즈입니다."),
    UNAUTHORIZED_ACTION(403, "권한이 없습니다.");

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
