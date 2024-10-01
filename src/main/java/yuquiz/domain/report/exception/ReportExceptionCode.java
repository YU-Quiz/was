package yuquiz.domain.report.exception;

import lombok.RequiredArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@RequiredArgsConstructor
public enum ReportExceptionCode implements ExceptionCode {
    ALREADY_REPORTED(409, "이미 신고한 퀴즈입니다."),
    REQUIRED_REASON(400, "기타 유형의 신고 사유는 필수 입니다.");

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
