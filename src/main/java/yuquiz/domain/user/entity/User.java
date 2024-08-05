package yuquiz.domain.user.entity;

import jakarta.persistence.*;
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

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(name = "major_name")
    private String majorName;

    @Column(name = "agree_email")
    private boolean agreeEmail;

    @Column(name = "banned_cnt")
    private int bannedCnt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "writer")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TriedQuiz> triedQuizzes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PinnedQuiz> pinnedQuizzes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
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
}
