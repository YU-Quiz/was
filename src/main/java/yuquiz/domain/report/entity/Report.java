package yuquiz.domain.report.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yuquiz.common.entity.BaseTimeEntity;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", length = 50)
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Builder
    public Report(String reason, ReportType reportType, Quiz quiz, User user) {
        this.reason = reason;
        this.reportType = reportType;
        this.quiz = quiz;
        this.reporter = user;
    }
}
