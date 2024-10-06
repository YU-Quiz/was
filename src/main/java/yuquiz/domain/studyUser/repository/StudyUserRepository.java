package yuquiz.domain.studyUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.studyUser.entity.StudyUser;

public interface StudyUserRepository extends JpaRepository<StudyUser, Long> {
    boolean existsByStudy_IdAndUser_Id(Long studyId, Long userId);
}
