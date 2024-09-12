package yuquiz.common.mail;

public interface MailProperties {
    String MAIL_SENDER = "YU-Quiz";
    String CODE_SUBJECT = "[YU-Quiz] 인증번호 메일입니다.";
    String CODE_TEXT = "[YU-Quiz] \n 인증번호 : ";
    String PASS_SUBJECT = "[YU-Quiz] 비밀번호를 재설정 해주세요.";
    String PASS_TEXT = "아래의 링크에 들어가 비밀번호를 재설정 해주세요. \n";
}
