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
import yuquiz.common.exception.JwtBlackListException;
import yuquiz.common.exception.JwtExpiredException;
import yuquiz.common.exception.dto.ExceptionsRes;
import yuquiz.common.exception.exceptionCode.ExceptionCode;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.domain.user.exception.UserExceptionCode;

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
            return sendErrorMessage(UserExceptionCode.UNAUTHORIZED_ACTION);
        }

        if (rootCause instanceof JwtBlackListException) {
            return sendErrorMessage(JwtExceptionCode.BLACKLIST_ACCESS_TOKEN);
        }

        if (rootCause instanceof JwtExpiredException) {
            return sendErrorMessage(JwtExceptionCode.ACCESS_TOKEN_EXPIRED);
        }

        if (rootCause instanceof JwtException) {
            return sendErrorMessage(JwtExceptionCode.INVALID_ACCESS_TOKEN);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> sendErrorMessage(ExceptionCode exceptionCode) {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.ERROR);
        ExceptionsRes exceptionsRes = ExceptionsRes.of(exceptionCode.getStatus(), exceptionCode.getMessage());

        String json = "";
        try {
            json = objectMapper.writeValueAsString(exceptionsRes);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert ErrorResponse to JSON", e);
        }

        return MessageBuilder.createMessage(json.getBytes(StandardCharsets.UTF_8),
                headers.getMessageHeaders());
    }
}