package yuquiz.domain.auth.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.mail.MailType;
import yuquiz.common.mail.service.MailService;
import yuquiz.common.utils.redis.RedisUtil;

import java.util.UUID;

import static yuquiz.common.utils.redis.RedisProperties.PASS_EXPIRATION_TIME;
import static yuquiz.common.utils.redis.RedisProperties.PASS_KEY_PREFIX;

@RequiredArgsConstructor
@Service
public class ResetPasswordService {

    private final MailService mailService;
    private final RedisUtil redisUtil;

    /* 메일에 비밀번호 재설정 링크 보내기 */
    public void sendPassResetLinkToMail(String email) {

        String randomUUID = makeUUID();
        saveUUID(randomUUID);
        mailService.sendMail(email, randomUUID, MailType.PASS);
    }

    /* 랜덤 UUID 생성 */
    private String makeUUID() {
        return String.valueOf(UUID.randomUUID());
    }

    /* UUID 저장 */
    private void saveUUID(String uuid) {

        String key = PASS_KEY_PREFIX + uuid;
        redisUtil.set(key, uuid);
        redisUtil.expire(key, PASS_EXPIRATION_TIME);
    }
}
