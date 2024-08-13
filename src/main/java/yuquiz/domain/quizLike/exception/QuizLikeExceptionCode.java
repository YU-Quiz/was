package yuquiz.domain.quizLike.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum QuizLikeExceptionCode implements ExceptionCode {

    ALREADY_EXIST(409, "이미 좋아요한 퀴즈입니다.");

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
