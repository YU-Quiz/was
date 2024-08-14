package yuquiz.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.user.entity.OAuth;
import yuquiz.domain.user.entity.OAuthPlatform;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {

    boolean existsByPlatformAndEmail(OAuthPlatform platform, String email);
}
