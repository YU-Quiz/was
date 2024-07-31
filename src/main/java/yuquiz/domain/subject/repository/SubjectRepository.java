package yuquiz.domain.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.subject.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
