package yuquiz.common.mail.strategy;

public interface MailStrategy {

    String getSubject();
    String generateContent(String content);
}
