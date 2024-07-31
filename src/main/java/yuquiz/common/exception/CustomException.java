package yuquiz.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;
}

