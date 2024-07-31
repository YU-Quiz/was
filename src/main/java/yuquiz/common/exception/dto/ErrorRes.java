package yuquiz.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@Getter
@AllArgsConstructor
public class ErrorRes {

    private int status;
    private String message;

    public static ErrorRes fromEntity(ExceptionCode exceptionCode) {
        return new ErrorRes(exceptionCode.getStatus(), exceptionCode.getMessage());
    }
}
