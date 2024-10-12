package yuquiz.domain.series.dto;

import yuquiz.domain.series.entity.Series;

public record SeriesSummaryRes(
        Long id,
        String name,
        String creator
) {
    public static SeriesSummaryRes fromEntity(Series series) {
        return new SeriesSummaryRes(
                series.getId(),
                series.getName(),
                series.getCreator().getNickname()
        );
    }
}
