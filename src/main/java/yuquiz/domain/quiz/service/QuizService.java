package yuquiz.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.domain.quiz.dto.QuizRes;
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.domain.quiz.dto.SortType;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;
import yuquiz.domain.subject.entity.Subject;
import yuquiz.domain.subject.exception.SubjectExceptionCode;
import yuquiz.domain.subject.repository.SubjectRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    private static final Integer POST_PER_PAGE = 20;

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

    public boolean gradeQuiz(Long quizId, String answer) {
        Quiz quiz = findQuizByQuizId(quizId);

        return quiz.getAnswer().equals(answer);
    }

    public String getAnswer(Long quizId) {
        return findQuizByQuizId(quizId).getAnswer();
    }

    public Page<QuizSummaryRes> getQuizzesBySubject(Long subjectId, SortType sort, Integer page) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID));

        Pageable pageable = PageRequest.of(page, POST_PER_PAGE, sort.getSort());
        Page<Quiz> quizzes = quizRepository.findAllBySubject(subject, pageable);

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

    public Page<QuizSummaryRes> getQuizzesByKeyword(String keyword, SortType sort, Integer page) {
        Pageable pageable = PageRequest.of(page, POST_PER_PAGE, sort.getSort());

        Page<Quiz> quizzes = quizRepository.findAllByTitleContainingOrQuestionContaining(keyword, keyword, pageable);

        return quizzes.map(QuizSummaryRes::fromEntity);
    }
}
