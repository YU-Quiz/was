package yuquiz.domain.study.dto;

import yuquiz.domain.study.entity.StudyState;

import java.time.LocalDateTime;

public record StudySummaryRes(
        String name,
        String leaderName,
        int maxUser,
        int curUser,
        LocalDateTime registerDuration,
        StudyState state
) {
}
