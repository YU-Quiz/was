package yuquiz.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yuquiz.domain.study.entity.Study;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long>, CustomStudyRepository{
    @Query("SELECT s.leader.id FROM Study s WHERE s.id = :studyId")
    Optional<Long> findLeaderById(Long studyId);
}
