package yuquiz.domain.chatRoom.websocket.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {


        Throwable rootCause = ex.getCause();

        if (rootCause instanceof AccessDeniedException) {
            return sendErrorMessage(ErrorCode.UNAUTHORIZED);
        }

        if (rootCause instanceof JwtException) {
            return sendErrorMessage(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        if (isJwtException(rootCause)) {
            return sendErrorMessage(ErrorCode.BLACKLIST_ACCESS_TOKEN);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private boolean isJwtException(Throwable rootCause) {
        return rootCause instanceof JwtHException;
    }

    private Message<byte[]> sendErrorMessage(ErrorCode errorCode) {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.ERROR);
        ErrorDto errorDto = new ErrorDto(errorCode.getStatus(), errorCode.getMessage());

        String json = "";
        try {
            json = objectMapper.writeValueAsString(errorDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert ErrorResponse to JSON", e);
        }

        return MessageBuilder.createMessage(json.getBytes(StandardCharsets.UTF_8),
                headers.getMessageHeaders());
    }
}