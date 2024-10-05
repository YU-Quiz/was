package yuquiz.domain.series.dto;

import yuquiz.domain.series.entity.Series;
import yuquiz.domain.study.entity.Study;

import java.util.Optional;

public record SeriesRes(
        Long id,
        String name,
        String creator,
        String studyName
) {
    public static SeriesRes fromEntity(Series series) {
        String studyName = Optional.ofNullable(series.getStudy())
                .map(Study::getStudyName)
                .orElse(null);

        return new SeriesRes(
                series.getId(),
                series.getName(),
                series.getCreator().getNickname(),
                studyName
        );
    }
}
