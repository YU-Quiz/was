package yuquiz.domain.pinnedQuiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.pinnedQuiz.entity.PinnedQuiz;
import yuquiz.domain.pinnedQuiz.repository.PinnedQuizRepository;
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;
import yuquiz.domain.triedQuiz.repository.TriedQuizRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class PinnedQuizService {
    private final PinnedQuizRepository pinnedQuizRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final TriedQuizRepository triedQuizRepository;

    private static final Integer QUIZ_PER_PAGE = 20;

    public Page<QuizSummaryRes> getPinnedQuizzes(Long userId, Integer page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Pageable pageable = PageRequest.of(page, QUIZ_PER_PAGE);

        return pinnedQuizRepository.findAllByUser(user, pageable)
                .map(pinnedQuiz -> QuizSummaryRes.fromEntity(pinnedQuiz.getQuiz(), triedQuizRepository.findIsSolvedByUserAndQuiz(user, pinnedQuiz.getQuiz())));
    }

    @Transactional
    public void pinQuiz(Long userId, Long quizId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));

        if (pinnedQuizRepository.existsByUserAndQuiz(user, quiz)) {
            throw new CustomException(QuizExceptionCode.ALREADY_PINNED);
        }

        PinnedQuiz pinnedQuiz = PinnedQuiz.builder()
                .user(user)
                .quiz(quiz)
                .build();

        pinnedQuizRepository.save(pinnedQuiz);
    }

    @Transactional
    public void deletePinQuiz(Long userId, Long quizId) {

        pinnedQuizRepository.deleteByUserIdAndQuizId(userId, quizId);
    }
}
