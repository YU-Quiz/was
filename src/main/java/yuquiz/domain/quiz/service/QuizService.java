package yuquiz.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.dto.QuizCreateReq;
import yuquiz.domain.quiz.entity.Quiz;
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
    public void quizCreate(QuizCreateReq quizCreateReq, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERNAME));

        Subject subject = subjectRepository.findById(quizCreateReq.getSubjectId())
                .orElseThrow(() -> new CustomException(SubjectExceptionCode.INVALID_ID));

        quizCreateReq.setWriter(user);
        quizCreateReq.setSubject(subject);

        Quiz quiz = quizCreateReq.toEntity();
        quizRepository.save(quiz);
    }
}
