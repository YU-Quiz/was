package yuquiz.domain.category.dto;

import yuquiz.domain.category.entity.Category;

public record CategorySummaryRes(
        Long id,
        String categoryName
) {
    public static CategorySummaryRes fromEntity(Category category) {
        return new CategorySummaryRes(
                category.getId(),
                category.getCategoryName()
        );
    }
}
