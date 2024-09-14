package yuquiz.mail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import yuquiz.common.mail.service.MailService;
import yuquiz.common.mail.strategy.CodeMailStrategy;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;


    @Test
    @DisplayName("메일 보내기 테스트")
    void sendMailTest() {
        // given
        String email = "your-mail@gmail.com";
        String code = "123456";

        // when
        mailService.sendMail(email, code, new CodeMailStrategy());

        // then
    }
}