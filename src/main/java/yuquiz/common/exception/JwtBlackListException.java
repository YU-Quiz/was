package yuquiz.common.exception;

import yuquiz.common.exception.exceptionCode.ExceptionCode;

public class JwtBlackListException extends RuntimeException {

    public JwtBlackListException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
    }
}
