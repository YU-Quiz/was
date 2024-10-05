package yuquiz.domain.series.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import yuquiz.domain.series.entity.Series;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.user.entity.User;

public record SeriesReq(
        @NotBlank(message = "문제집 이름은 필수 입력입니다.")
        String name,
        @Min(value = 1, message = "스터디ID는 1 이상이어야 합니다.")
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
