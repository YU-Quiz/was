package yuquiz.common.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.GlobalExceptionCode;
import yuquiz.common.mail.strategy.MailStrategy;

import java.io.UnsupportedEncodingException;

import static yuquiz.common.mail.MailProperties.MAIL_SENDER;

@RequiredArgsConstructor
@EnableAsync
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    /* 메일 보내기 */
    @Async
    public void sendMail(String email, String content, MailStrategy strategy) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setBcc(email);
            helper.setSubject(strategy.getSubject());
            helper.setText(strategy.generateContent(content), true);
            helper.setFrom(new InternetAddress(emailUsername, MAIL_SENDER, "UTF-8"));

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}