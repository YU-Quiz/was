package yuquiz.domain.series.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.series.entity.Series;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("select s.creator.id from Series s where s.id = :id")
    Optional<Long> findCreatorIdById(@Param("id") Long id);
}
