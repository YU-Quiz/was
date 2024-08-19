package yuquiz.domain.pinnedQuiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.pinnedQuiz.dto.PinnedQuizSummaryRes;
import yuquiz.domain.pinnedQuiz.repository.PinnedQuizRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class PinnedQuizService {
    private final PinnedQuizRepository pinnedQuizRepository;
    private final UserRepository userRepository;

    private static final Integer QUIZ_PER_PAGE = 20;

    public Page<PinnedQuizSummaryRes> getPinnedQuizzes(Long userId, Integer page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Pageable pageable = PageRequest.of(page, QUIZ_PER_PAGE);

        return pinnedQuizRepository.findAllByUser(user, pageable)
                .map(pinnedQuiz -> PinnedQuizSummaryRes.fromEntity(pinnedQuiz.getQuiz()));
    }
}
