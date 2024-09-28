package yuquiz.domain.study.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.common.entity.BaseTimeEntity;
import yuquiz.domain.chatRoom.entity.ChatRoom;
import yuquiz.domain.studyUser.entity.StudyUser;

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

    @Builder
    public Study(String studyName, ChatRoom chatRoom) {
        this.studyName = studyName;
        this.chatRoom = chatRoom;
    }
}
