package yuquiz.domain.quiz.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.dto.QuizRes;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;

@Service
@RequiredArgsConstructor
public class AdminQuizService {

    private final QuizRepository quizRepository;

    private static final Integer QUIZ_PER_PAGE = 10;

    public Page<QuizRes> getQuizPage(Integer pageNumber){

        Pageable pageable = PageRequest.of(pageNumber, QUIZ_PER_PAGE);
        Page<Quiz> page = quizRepository.findAllByOrderByCreatedAtDesc(pageable);

        return page.map(quiz -> QuizRes.from(quiz));
    }

    @Transactional
    public void deleteQuiz(Long quizId){

        try{
           quizRepository.deleteById(quizId);
        } catch (EmptyResultDataAccessException e) {
           throw new CustomException(QuizExceptionCode.INVALID_ID);
        }
    }
}
