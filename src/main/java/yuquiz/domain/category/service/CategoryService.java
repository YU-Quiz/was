package yuquiz.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.domain.category.dto.CategorySummaryRes;
import yuquiz.domain.category.entity.Category;
import yuquiz.domain.category.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategorySummaryRes> getCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategorySummaryRes::fromEntity)
                .collect(Collectors.toList());
    }
}
