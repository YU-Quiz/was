package yuquiz.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yuquiz.common.exception.dto.ErrorRes;
import yuquiz.common.exception.exceptionCode.ExceptionCode;
import yuquiz.common.exception.exceptionCode.GlobalExceptionCode;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* CustomHandler 에러 처리 */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> customExceptionHandler(CustomException ex) {

        ExceptionCode exceptionCode = ex.getExceptionCode();
        ErrorRes errorRes = ErrorRes.fromEntity(exceptionCode);
        log.error("Error occurred : {}, Stack trace: {}", ex.getMessage(), getCustomStackTrace(ex));
        return ResponseEntity.status(errorRes.getStatus()).body(errorRes);
    }

    /* 유효성 검사 예외 처리 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.error("Error occurred : {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /* 일반 예외 처리 */
    @ExceptionHandler
    protected ResponseEntity<?> customServerException(Exception ex) {

        ExceptionCode exceptionCode = GlobalExceptionCode.INTERNAL_SERVER_ERROR;
        ErrorRes errorRes = ErrorRes.fromEntity(exceptionCode);
        log.error("Error occurred : {}, Stack trace: {}", ex.getMessage(), getCustomStackTrace(ex));
        return ResponseEntity.status(errorRes.getStatus()).body(errorRes);
    }

    /* 오류 코드 5줄 불러오기 */
    public String getCustomStackTrace(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(stackTrace.length, 5); i++) {
            sb.append(stackTrace[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
