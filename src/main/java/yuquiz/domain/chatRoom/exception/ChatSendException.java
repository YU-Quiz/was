package yuquiz.domain.chatRoom.exception;

import yuquiz.common.exception.exceptionCode.ExceptionCode;

public class ChatSendException extends RuntimeException {

    public ChatSendException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
    }
}
