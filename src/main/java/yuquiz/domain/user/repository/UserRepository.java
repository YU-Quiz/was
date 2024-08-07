package yuquiz.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
