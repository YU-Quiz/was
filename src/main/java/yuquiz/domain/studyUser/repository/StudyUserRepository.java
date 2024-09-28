package yuquiz.domain.studyUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.studyUser.entity.StudyUser;

public interface StudyUserRepository extends JpaRepository<StudyUser, Long> {
}
