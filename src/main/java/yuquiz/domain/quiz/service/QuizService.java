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
    public void createQuiz(QuizReq quizReq, Principal principal) {
        User user = findUserByPrincipal(principal);

        Subject subject = subjectRepository.findById(quizReq.getSubjectId())
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID));

        quizReq.setWriter(user);
        quizReq.setSubject(subject);

        Quiz quiz = quizReq.toEntity();
        quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long quizId, Principal principal) {
        Quiz quiz = findQuizByIdAndValidateUser(quizId, principal);
        quizRepository.delete(quiz);
    }

    @Transactional
    public void updateQuiz(Long quizId, QuizReq quizReq, Principal principal) {
        Quiz quiz = findQuizByIdAndValidateUser(quizId, principal);

        quizReq.setSubject(subjectRepository.findById(quizReq.getSubjectId())
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID)));
        quiz.update(quizReq);
    }

    private User findUserByPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERNAME));
    }

    private Quiz findQuizByIdAndValidateUser(Long quizId, Principal principal) {
        User user = findUserByPrincipal(principal);
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));

        if (!quiz.getWriter().equals(user)) {
            throw new CustomException(QuizExceptionCode.UNAUTHORIZED_ACTION);
        }

        return quiz;
    }
}
