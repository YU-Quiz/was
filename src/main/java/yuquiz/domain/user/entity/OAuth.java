package yuquiz.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Column(name = "platform_id")
    private String platformId;

    @Enumerated(EnumType.STRING)
    private OAuthPlatform platform;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public OAuth(String email, String platformId, OAuthPlatform platform, User user) {
        this.email = email;
        this.platformId = platformId;
        this.platform = platform;
        this.user = user;
    }

}
