package yuquiz.domain.triedQuiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TriedQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_solved")
    private Boolean isSolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Builder
    public TriedQuiz(Boolean isSolved, User user, Quiz quiz) {
        this.isSolved = isSolved;
        this.user = user;
        this.quiz = quiz;
    }

    public void updateIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }
}
