package yuquiz.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OAuthPlatform {
    KAKAO("KAKAO"),
    NAVER("NAVER");

    private String name;


}
