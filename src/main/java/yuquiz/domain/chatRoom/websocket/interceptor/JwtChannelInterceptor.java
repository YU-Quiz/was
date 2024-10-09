package yuquiz.domain.chatRoom.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import yuquiz.common.exception.JwtBlackListException;
import yuquiz.common.exception.JwtExpiredException;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.security.token.blacklist.BlackListTokenService;

import static yuquiz.common.utils.jwt.JwtProperties.ACCESS_HEADER_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final BlackListTokenService blackListTokenService;
//    private final ChatUserService chatUserService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 연결 요청시 JWT 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Authorization 헤더 추출
            String accessTokenInHeader = accessor.getFirstNativeHeader(ACCESS_HEADER_VALUE);
            String accessToken = passingAccessToken(accessTokenInHeader);

            isTokenValid(accessToken);

            Long userId = jwtProvider.getUserId(accessToken);
            Long roomId = Long.valueOf(accessor.getFirstNativeHeader("roomId"));

//            if (!chatUserService.isChatMember(userId, roomId)) {
//                throw new AccessDeniedException(UserExceptionCode.UNAUTHORIZED_ACTION.getMessage());
//            }

            accessor.getSessionAttributes().put("userId", userId);

        }
        return message;
    }

    /* 토큰 유효성 검사 */
    private void isTokenValid(String accessToken) {

        if (blackListTokenService.existsBlackListCheck(accessToken)) {       // AccessToken이 블랙리스트에 있는지.
            throw new JwtBlackListException(JwtExceptionCode.BLACKLIST_ACCESS_TOKEN);
        }

        if (jwtProvider.isExpired(accessToken)) {   // 만료 되었는지
            throw new JwtExpiredException(JwtExceptionCode.ACCESS_TOKEN_EXPIRED);
        }
    }

    /* 토큰 파싱 */
    private String passingAccessToken(String accessTokenInHeader) {
        if (accessTokenInHeader == null || !accessTokenInHeader.startsWith(TOKEN_PREFIX)) {
            throw new RuntimeException("토큰이 유효하지 않음");
        }
        return accessTokenInHeader.substring(TOKEN_PREFIX.length()).trim();
    }
}