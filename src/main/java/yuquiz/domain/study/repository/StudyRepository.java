package yuquiz.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.study.entity.Study;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
