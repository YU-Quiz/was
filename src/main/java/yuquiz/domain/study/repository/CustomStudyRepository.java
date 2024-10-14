package yuquiz.domain.study.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yuquiz.domain.study.dto.StudyFilter;
import yuquiz.domain.study.dto.StudySortType;
import yuquiz.domain.study.entity.Study;

public interface CustomStudyRepository {
    Page<Study> getStudies(String keyword, Pageable pageable, StudySortType sort, StudyFilter filter);
}
