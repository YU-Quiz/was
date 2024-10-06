package yuquiz.domain.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.common.entity.BaseTimeEntity;
import yuquiz.domain.chatRoom.entity.ChatRoom;
import yuquiz.domain.studyUser.entity.StudyUser;
import yuquiz.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "study_name")
    private String studyName;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
    private List<StudyUser> studyUsers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @Builder
    public Study(String studyName, ChatRoom chatRoom, User leader) {
        this.studyName = studyName;
        this.chatRoom = chatRoom;
        this.leader = leader;
    }
}
