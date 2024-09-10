package yuquiz.domain.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.domain.category.dto.CategorySummaryRes;
import yuquiz.domain.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {

        List<CategorySummaryRes> categories = categoryService.getCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
