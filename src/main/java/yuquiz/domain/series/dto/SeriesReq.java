package yuquiz.domain.series.dto;

import jakarta.validation.constraints.NotBlank;
import yuquiz.domain.series.entity.Series;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.user.entity.User;

public record SeriesReq(
        @NotBlank
        String name,
        Long studyId
) {

    public Series toEntity(User user, Study study) {
        return Series.builder()
                .name(this.name)
                .creator(user)
                .study(study)
                .build();
    }
}
