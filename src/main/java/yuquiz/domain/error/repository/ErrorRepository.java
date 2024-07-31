package yuquiz.domain.error.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.error.entity.Error;

public interface ErrorRepository extends JpaRepository<Error, Long> {
}
