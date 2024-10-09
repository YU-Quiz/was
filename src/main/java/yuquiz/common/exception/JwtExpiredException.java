package yuquiz.common.exception;

import yuquiz.common.exception.exceptionCode.ExceptionCode;

public class JwtExpiredException extends RuntimeException {

    public JwtExpiredException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
    }
}
