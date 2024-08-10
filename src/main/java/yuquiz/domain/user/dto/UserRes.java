package yuquiz.domain.user.dto;

import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

public record UserRes(
        Long id,
        String username,
        String nickname,
        String email,
        LocalDateTime createdAt
) {
    public static UserRes fromEntity(User user) {
        return new UserRes(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
