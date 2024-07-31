package yuquiz.domain.major.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.major.entity.Major;

public interface MajorRepository extends JpaRepository<Major, Long> {
}
