package yuquiz.domain.quiz.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.quiz.dto.quiz.AdminQuizSummaryRes;
import yuquiz.domain.quiz.dto.quiz.QuizSortType;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.repository.QuizRepository;

@Service
@RequiredArgsConstructor
public class AdminQuizService {

    private final QuizRepository quizRepository;

    private static final Integer QUIZ_PER_PAGE = 20;

    public Page<AdminQuizSummaryRes> getAllQuizzes(QuizSortType sort, Integer page) {

        Pageable pageable = PageRequest.of(page, QUIZ_PER_PAGE, sort.getSort());
        Page<Quiz> quizzes = quizRepository.findAll(pageable);

        return quizzes.map(AdminQuizSummaryRes::fromEntity);
    }

    @Transactional
    public void deleteQuiz(Long quizId){

        quizRepository.deleteById(quizId);
    }
}
