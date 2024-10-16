package yuquiz.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import yuquiz.domain.chatRoom.entity.ChatRoom;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.study.entity.StudyState;
import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

public record StudyReq(
        @NotBlank(message = "제목은 필수 입력입니다.")
        @Schema(description = "스터디 이름", example = "코딩 테스트 스터디")
        String name,

        @NotBlank(message = "설명은 필수 입력입니다.")
        @Schema(description = "스터디 설명", example = "코딩 테스트를 준비하기 위한 스터디입니다.")
        String description,

        @Schema(description = "스터디 신청 기간", example = "2024-10-06T12:00:00")
        LocalDateTime registerDuration,

        @NotNull(message = "최대 인원은 필수 입력입니다.")
        @Min(value = 2, message = "최소 인원은 2명입니다.")
        @Schema(description = "스터디 참가 최대 인원", example = "5")
        Integer maxUser,

        StudyState state

) {
    //todo : ChatRoom 개발 되면 추가하기
    public Study toEntity(User leader, ChatRoom chatRoom) {
        return Study.builder()
                .studyName(name)
                .description(description)
                .registerDuration(registerDuration)
                .maxUser(maxUser)
                .chatRoom(chatRoom)
                .leader(leader)
                .build();
    }
}
