package yuquiz.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndEmail(String username, String email);

    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String findPasswordById(@Param("id") Long id);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void updatePasswordByUserId(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("update User u set u.password = :password where u.username = :username")
    void updatePasswordByUsername(@Param("username") String username, @Param("password") String password);
}
