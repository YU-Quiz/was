package yuquiz.domain.study.dto;

import yuquiz.domain.studyUser.entity.StudyUser;

import java.time.LocalDateTime;

public record StudyRequestRes(
        Long userId,
        String name,
        LocalDateTime requestAt

) {
    public static StudyRequestRes fromEntity(StudyUser studyUser) {
        return new StudyRequestRes(
                studyUser.getUser().getId(),
                studyUser.getUser().getNickname(),
                studyUser.getJoinedAt()
        );
    }
}
