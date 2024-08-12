package yuquiz.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
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

    @Transactional
    public void reportQuiz(Long quizId, ReportReq reportReq) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));

        Report report = reportReq.toEntity(quiz);

        reportRepository.save(report);
    }
}
