package yuquiz.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.dto.QuizReq;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;
import yuquiz.domain.subject.entity.Subject;
import yuquiz.domain.subject.exception.SubjectExceptionCode;
import yuquiz.domain.subject.repository.SubjectRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public void createQuiz(QuizReq quizReq, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(UserExceptionCode.INVALID_USERID));

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

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));
    }

    private Quiz findQuizByIdAndValidateUser(Long quizId, Long userId) {
        User user = findUserByUserId(userId);
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));

        if (!quiz.getWriter().equals(user)) {
            throw new CustomException(QuizExceptionCode.UNAUTHORIZED_ACTION);
        }

        return quiz;
    }
}
