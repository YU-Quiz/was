package yuquiz.common.mail.strategy;

import org.springframework.stereotype.Component;

import static yuquiz.common.mail.MailProperties.CODE_SUBJECT;
import static yuquiz.common.mail.MailProperties.CODE_TEXT;

@Component
public class CodeMailStrategy implements MailStrategy {

    @Override
    public String getSubject() {
        return CODE_SUBJECT;
    }

    @Override
    public String generateContent(String code) {
        return CODE_TEXT + code;
    }
}
