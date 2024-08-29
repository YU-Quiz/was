package yuquiz.common.mail;

public interface MailProperties {
    String MAIL_SENDER = "YU-Quiz";
    String CODE_SUBJECT = "[YU-Quiz] 인증번호 메일입니다.";
    String CODE_TEXT = "[YU-Quiz] \n 인증번호 : ";
    String PASS_SUBJECT = "[YU-Quiz] 임시 비밀번호입니다.";
    String PASS_TEXT = "[YU-Quiz] \n 로그인 한 후 회원 정보 수정에서 비밀번호를 변경해 주세요. \n" +
            "임시 비밀번호 : ";
}
