package yuquiz.common.exception;

import yuquiz.common.exception.exceptionCode.ExceptionCode;

public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;
    private final String additionalMessage;

    public CustomException(ExceptionCode exceptionCode) {
        this(exceptionCode, null);
    }

    public CustomException(ExceptionCode exceptionCode, String additionalMessage) {
        this.exceptionCode = exceptionCode;
        this.additionalMessage = additionalMessage;
    }

    @Override
    public String getMessage() {
        if (additionalMessage != null) {
            return exceptionCode.getMessage() + " - " + additionalMessage;
        }
        return exceptionCode.getMessage();
    }

    public int getStatus() {
        return exceptionCode.getStatus();
    }
}

