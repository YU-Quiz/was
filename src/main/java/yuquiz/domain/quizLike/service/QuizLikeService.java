package yuquiz.domain.quizLike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.dto.QuizSummaryRes;
import yuquiz.domain.quizLike.repository.QuizLikeRepository;
import yuquiz.domain.triedQuiz.repository.TriedQuizRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuizLikeService {

    private final QuizLikeRepository quizLikeRepository;
    private final UserRepository userRepository;
    private final TriedQuizRepository triedQuizRepository;

    private static final Integer QUIZ_PER_PAGE = 20;

    public Page<QuizSummaryRes> getLikedQuizzes(Long userId, Integer page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Pageable pageable = PageRequest.of(page, QUIZ_PER_PAGE);

        return quizLikeRepository.findAllByUser(user, pageable)
                .map(quizLike -> QuizSummaryRes.fromEntity(quizLike.getQuiz(), triedQuizRepository.findIsSolvedByUserAndQuiz(user, quizLike.getQuiz())));
    }

}
