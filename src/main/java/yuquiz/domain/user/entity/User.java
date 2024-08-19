package yuquiz.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.common.entity.BaseTimeEntity;
import yuquiz.domain.comment.entity.Comment;
import yuquiz.domain.notification.entity.Notification;
import yuquiz.domain.pinnedQuiz.entity.PinnedQuiz;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quizLike.entity.QuizLike;
import yuquiz.domain.triedQuiz.entity.TriedQuiz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(name = "major_name")
    private String majorName;

    @Column(name = "agree_email")
    private boolean agreeEmail;

    @Column(name = "banned_cnt")
    private int bannedCnt;

    @Column(name = "unlocked_at")
    @JsonIgnore
    private LocalDateTime unlockedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Notification> notifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Quiz> quizzes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<TriedQuiz> triedQuizzes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PinnedQuiz> pinnedQuizzes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<QuizLike> quizLikes = new ArrayList<>();

    @Builder
    public User(String username, String password, String nickname,
                String email, String majorName, boolean agreeEmail, Role role) {

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.majorName = majorName;
        this.agreeEmail = agreeEmail;
        this.role = role;
    }

    /* 사용자 업데이트 편의 메서드 */
    public void updateUser(String nickname, String email, boolean agreeEmail, String majorName) {
        this.nickname = nickname;
        this.email = email;
        this.agreeEmail = agreeEmail;
        this.majorName = majorName;
    }

    /* 비밀번호 변경 편의 메서드 */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    /* 사용자 정지 상태 조작 메서드 */
    public void updateSuspendStatus(LocalDateTime unlockedAt, int bannedCnt){
        this.unlockedAt = unlockedAt;
        this.bannedCnt = bannedCnt;
    }
}
