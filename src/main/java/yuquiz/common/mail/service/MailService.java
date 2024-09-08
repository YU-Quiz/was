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
import yuquiz.common.mail.MailType;

import java.io.UnsupportedEncodingException;

import static yuquiz.common.mail.MailProperties.CODE_SUBJECT;
import static yuquiz.common.mail.MailProperties.CODE_TEXT;
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
    public void sendMail(String email, String content, MailType type) {

        MimeMessage message = null;

        try {
            switch (type) {
                case CODE -> message = getCodeMessage(email, content);
            }
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    /* Code Message 만들기. */
    private MimeMessage getCodeMessage(String email, String code) throws MessagingException, UnsupportedEncodingException  {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setBcc(email);
        helper.setSubject(CODE_SUBJECT);
        helper.setText(CODE_TEXT + code, true);
        helper.setFrom(new InternetAddress(emailUsername, MAIL_SENDER, "UTF-8"));

        return message;
    }
}