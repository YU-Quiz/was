package yuquiz.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.domain.quiz.dto.QuizRes;
import yuquiz.domain.quiz.dto.QuizSortType;
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;
import yuquiz.domain.report.dto.ReportReq;
import yuquiz.domain.report.entity.Report;
import yuquiz.domain.report.repository.ReportRepository;
import yuquiz.domain.subject.entity.Subject;
import yuquiz.domain.subject.exception.SubjectExceptionCode;
import yuquiz.domain.subject.repository.SubjectRepository;
import yuquiz.domain.triedQuiz.entity.TriedQuiz;
import yuquiz.domain.triedQuiz.repository.TriedQuizRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TriedQuizRepository triedQuizRepository;

    private static final Integer QUIZ_PER_PAGE = 20;

    @Transactional
    public void createQuiz(QuizReq quizReq, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Subject subject = subjectRepository.findById(quizReq.subjectId())
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID));


        Quiz quiz = quizReq.toEntity(user, subject);
        quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long quizId, Long userId) {
        Quiz quiz = findQuizByIdAndValidateUser(quizId, userId);
        quizRepository.delete(quiz);
    }

    @Transactional
    public void updateQuiz(Long quizId, QuizReq quizReq, Long userId) {
        Quiz quiz = findQuizByIdAndValidateUser(quizId, userId);

        Subject subject = subjectRepository.findById(quizReq.subjectId())
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID));

        quiz.update(quizReq, subject);
    }

    public QuizRes getQuizById(Long quizId) {
        Quiz quiz = findQuizByQuizId(quizId);

        return QuizRes.fromEntity(quiz);
    }

    @Transactional
    public boolean gradeQuiz(Long userId, Long quizId, String answer) {
        Quiz quiz = findQuizByQuizId(quizId);
        User user = findUserByUserId(userId);

        boolean isSolved = quiz.getAnswer().equals(answer);

        TriedQuiz triedQuiz = triedQuizRepository.findByUserAndQuiz(user, quiz)
                .orElse(new TriedQuiz(isSolved, user, quiz));

        triedQuiz.updateIsSolved(isSolved);
        triedQuizRepository.save(triedQuiz);

        return isSolved;
    }

    public String getAnswer(Long quizId) {
        return findQuizByQuizId(quizId).getAnswer();
    }

    public Page<QuizSummaryRes> getQuizzesBySubject(Long subjectId, QuizSortType sort, Integer page) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID));


        Pageable pageable = PageRequest.of(page, QUIZ_PER_PAGE, sort.getSort());
        Page<Quiz> quizzes = quizRepository.findAllBySubject(subject, pageable);

        return quizzes.map(QuizSummaryRes::fromEntity);
    }

    public Page<QuizSummaryRes> getQuizzesByKeyword(String keyword, QuizSortType sort, Integer page) {
        Pageable pageable = PageRequest.of(page, QUIZ_PER_PAGE, sort.getSort());

        Page<Quiz> quizzes = quizRepository.findAllByTitleContainingOrQuestionContaining(keyword, keyword, pageable);

        return quizzes.map(QuizSummaryRes::fromEntity);
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));
    }

    private Quiz findQuizByQuizId(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));
    }

    private Quiz findQuizByIdAndValidateUser(Long quizId, Long userId) {
        User user = findUserByUserId(userId);
        Quiz quiz = findQuizByQuizId(quizId);


        if (!quiz.getWriter().equals(user)) {
            throw new CustomException(QuizExceptionCode.UNAUTHORIZED_ACTION);
        }

        return quiz;
    }
}
