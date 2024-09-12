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
import static yuquiz.common.mail.MailProperties.PASS_SUBJECT;
import static yuquiz.common.mail.MailProperties.PASS_TEXT;

@RequiredArgsConstructor
@EnableAsync
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${mail.reset-password.link}")
    private String resetPasswordLink;

    /* 메일 보내기 */
    @Async
    public void sendMail(String email, String content, MailType type) {

        MimeMessage message = null;

        try {
            switch (type) {
                case CODE -> message = getCodeMessage(email, content);
                case PASS -> message = getPassMessage(email, content);
            }
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    /* Code Message 만들기. */
    private MimeMessage getCodeMessage(String email, String code) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setBcc(email);
        helper.setSubject(CODE_SUBJECT);
        helper.setText(CODE_TEXT + code, true);
        helper.setFrom(new InternetAddress(emailUsername, MAIL_SENDER, "UTF-8"));

        return message;
    }

    /* 비밀번호 재설정 Message 만들기. */
    private MimeMessage getPassMessage(String email, String uuid) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String text = PASS_TEXT + "<p><a href=\"" + resetPasswordLink + "?code=" + uuid + "\">비밀번호 재설정</a></p>";
        helper.setBcc(email);
        helper.setSubject(PASS_SUBJECT);
        helper.setText(text, true);
        helper.setFrom(new InternetAddress(emailUsername, MAIL_SENDER, "UTF-8"));

        return message;
    }

}