package yuquiz.domain.series.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.series.entity.Series;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    
}
