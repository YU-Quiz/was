package yuquiz.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import yuquiz.common.mail.service.MailService;
import yuquiz.common.mail.strategy.MailStrategy;
import yuquiz.common.utils.redis.RedisUtil;

import java.util.Optional;
import java.util.UUID;

import static yuquiz.common.utils.redis.RedisProperties.PASS_EXPIRATION_TIME;
import static yuquiz.common.utils.redis.RedisProperties.PASS_KEY_PREFIX;

@RequiredArgsConstructor
@Service
public class ResetPasswordService {

    private final MailService mailService;
    private final RedisUtil redisUtil;
    @Qualifier("passMailStrategy")
    private final MailStrategy passMailStrategy;

    /* 메일에 비밀번호 재설정 링크 보내기 */
    public void sendPassResetLinkToMail(String email, String username) {

        String randomUUID = makeUUID();
        saveUUID(username, randomUUID);
        mailService.sendMail(email, randomUUID, passMailStrategy);
    }

    /* 랜덤 UUID 생성 */
    private String makeUUID() {
        return String.valueOf(UUID.randomUUID());
    }

    /* UUID 저장 */
    private void saveUUID(String username, String uuid) {

        String key = PASS_KEY_PREFIX + username;
        redisUtil.set(key, uuid);
        redisUtil.expire(key, PASS_EXPIRATION_TIME);
    }

    /* code 확인 */
    public boolean isValidCode(String username, String code) {

        String key = PASS_KEY_PREFIX + username;
        Optional<String> savedCode = Optional.ofNullable((String) redisUtil.get(key));

        if (savedCode.isEmpty() || !code.equals(savedCode.get())) {
            return false;
        }

        redisUtil.del(key);
        return true;
    }
}
