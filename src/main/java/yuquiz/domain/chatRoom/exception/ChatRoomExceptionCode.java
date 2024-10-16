package yuquiz.domain.chatRoom.exception;

import lombok.AllArgsConstructor;
import yuquiz.common.exception.exceptionCode.ExceptionCode;

@AllArgsConstructor
public enum ChatRoomExceptionCode implements ExceptionCode {

    UNAUTHORIZED_ACTION(403, "채팅방 입장 권한이 없습니다."),
    INVALID_ID(404, "존재하지 않는 채팅방입니다."),
    CANNOT_SEND_MESSAGE(403, "권한이 없어 메시지를 보낼 수 없습니다.");

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
