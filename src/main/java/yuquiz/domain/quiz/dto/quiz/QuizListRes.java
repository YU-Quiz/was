package yuquiz.domain.quiz.dto.quiz;

import lombok.Builder;

import java.util.List;

@Builder
public record QuizListRes(
        List<QuizSummaryRes> quizList,
        int page,
        long total,
        int totalPages
) {
    public static QuizListRes of(List<QuizSummaryRes> quizList,
                                 int page,
                                 long total,
                                 int totalPages) {
        return QuizListRes.builder()
                .quizList(quizList)
                .page(page)
                .total(total)
                .totalPages(totalPages)
                .build();
    }
}
