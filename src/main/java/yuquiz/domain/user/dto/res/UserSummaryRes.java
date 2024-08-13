package yuquiz.domain.user.dto.res;

import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

public record UserSummaryRes(
        Long id,
        String username,
        String nickname,
        String email,
        LocalDateTime createdAt
) {
    public static UserSummaryRes fromEntity(User user) {
        return new UserSummaryRes(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
