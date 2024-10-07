package yuquiz.domain.chatRoom.dto;

public record MessageReq(
        String roomId,
        String sender,
        String content,
        String createdAt,
        MessageType type
) {
}
