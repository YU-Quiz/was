package yuquiz.domain.quizSeries.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.quiz.exception.QuizExceptionCode;
import yuquiz.domain.quiz.repository.QuizRepository;
import yuquiz.domain.quizSeries.entity.QuizSeries;
import yuquiz.domain.quizSeries.exception.QuizSeriesExceptionCode;
import yuquiz.domain.quizSeries.repository.QuizSeriesRepository;
import yuquiz.domain.series.entity.Series;
import yuquiz.domain.series.exception.SeriesExceptionCode;
import yuquiz.domain.series.repository.SeriesRepository;

@Service
@RequiredArgsConstructor
public class QuizSeriesService {

    private final QuizSeriesRepository quizSeriesRepository;
    private final QuizRepository quizRepository;
    private final SeriesRepository seriesRepository;

    public void addQuiz(Long seriesId, Long quizId, Long userId) {

        if (!validateCreator(seriesId, userId)) {
            throw new CustomException(QuizSeriesExceptionCode.UNAUTHORIZED_ACTION);
        }

        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new CustomException(SeriesExceptionCode.INVALID_ID));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizExceptionCode.INVALID_ID));

        QuizSeries quizSeries = QuizSeries.builder()
                .series(series)
                .quiz(quiz)
                .build();

        quizSeriesRepository.save(quizSeries);
    }

    private boolean validateCreator(Long seriesId, Long userId) {
        return seriesRepository.findCreatorIdById(seriesId)
                .map(creatorId -> creatorId.equals(userId))
                .orElse(false);
    }
}
