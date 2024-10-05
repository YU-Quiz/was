package yuquiz.domain.series.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import yuquiz.domain.series.entity.Series;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.user.entity.User;

public record SeriesReq(
        @Schema(description = "문제집 이름", example = "땡땡이의 문제집")
        @NotBlank(message = "문제집 이름은 필수 입력입니다.")
        String name,
        @Schema(description = "스터디 ID", example = "1")
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
