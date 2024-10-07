package yuquiz.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.domain.chatRoom.dto.MessageReq;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
//    private final ChatUserService chatUserService;

    /* 방에 메시지 전송 */
    @MessageMapping("/message/{roomId}")    // 클라이언트가 서버로 보내는 메시지를 처리할 경로
    public MessageReq sendMessage(MessageReq messageReq, StompHeaderAccessor accessor) {

        Long userId = (Long) accessor.getSessionAttributes().get("userId");

//        if (chatUserService.isChatMember(userId, Long.valueOf(messageReq.getRoomId()))) {
//            log.error("권한 없음");
//        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), messageReq);
        return messageReq;
    }

    /* 채팅방에 유저가 입장했을 때의 메시지 처리 */
    @MessageMapping("/user/{roomId}")
    public MessageReq addUser(MessageReq messageReq, StompHeaderAccessor accessor) {

        Long userId = (Long) accessor.getSessionAttributes().get("userId");

//        if (chatUserService.isChatMember(userId, Long.valueOf(messageReq.getRoomId()))) {
//            log.error("권한 없음");
//        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), messageReq);
        return messageReq;
    }
}