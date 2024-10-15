package yuquiz.domain.studyUser.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yuquiz.domain.chatRoom.entity.ChatRoom;
import yuquiz.domain.study.entity.Study;
import yuquiz.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class StudyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "joined_at")
    @CreatedDate
    private LocalDateTime joinedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private StudyRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 50)
    private UserState state;

    @Builder
    public StudyUser(User user, Study study, ChatRoom chatRoom, StudyRole role, UserState state) {
        this.user = user;
        this.study = study;
        this.chatRoom = chatRoom;
        this.role = role;
        this.state = state;
    }

    public void accept() {
        this.joinedAt = LocalDateTime.now();
        this.state = UserState.REGISTERED;
    }
}
