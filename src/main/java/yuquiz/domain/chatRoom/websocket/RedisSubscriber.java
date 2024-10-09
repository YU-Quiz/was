package yuquiz.domain.chatRoom.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import yuquiz.domain.chatRoom.dto.MessageReq;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            MessageReq roomMessageReq = objectMapper.readValue(publishMessage, MessageReq.class);

            simpMessageSendingOperations.convertAndSend("/sub/" + roomMessageReq.roomId(), roomMessageReq);
        } catch (IOException e) {
            log.error("Failed to convert roomMessageReq to JSON", e);
        }
    }
}