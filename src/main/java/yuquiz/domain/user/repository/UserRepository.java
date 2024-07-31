package yuquiz.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
