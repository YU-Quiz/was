package yuquiz.common.exception.dto;

public record ExceptionsRes(int status, String message) {
    public static ExceptionsRes of(int status, String message) {
        return new ExceptionsRes(status, message);
    }
}
