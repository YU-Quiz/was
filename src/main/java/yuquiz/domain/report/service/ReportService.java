package yuquiz.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.notification.dto.NotificationReq;
import yuquiz.domain.notification.dto.NotificationType;
import yuquiz.domain.notification.service.NotificationService;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;
import yuquiz.domain.report.dto.ReportReq;
import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.repository.ReportRepository;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final QuizRepository quizRepository;
    private final NotificationService notificationService;

    @Transactional
    public void reportQuiz(Long quizId, ReportReq reportReq) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));

        Report report = reportReq.toEntity(quiz);

        reportNotification(quiz.getTitle(), report.getReason(), quiz.getWriter().getId());

        if (reportRepository.countByQuiz(quiz) + 1 >= 5) {
            quiz.changeVisibility();
        }

        reportRepository.save(report);
    }

    public void reportNotification(String quizTitle, String reason, Long userId) {
        NotificationReq notificationReq = NotificationReq.builder()
                .title(quizTitle + " " + NotificationType.QUIZ_REPORT_NOTIFICATION.getTitle())
                .message(quizTitle + " " + NotificationType.QUIZ_REPORT_NOTIFICATION.getMessage() +
                        "\n사유 : " + reason)
                .redirectUrl("")
                .build();

        notificationService.createNotification(userId, notificationReq);

    }
}
