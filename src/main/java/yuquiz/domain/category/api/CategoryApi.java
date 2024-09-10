package yuquiz.domain.category.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "[게시글 카테고리 API]", description = "카테고리 관련 API")
public interface CategoryApi {

    @Operation(summary = "게시글 카테고리 조회", description = "카테고리의 Id와 이름을 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "categoryName": "공지게시판"
                                        },
                                        {
                                            "id": 2,
                                            "categoryName": "자유게시판"
                                        }
                                    ]
                                    """)
                    }))
    })
    ResponseEntity<?> getCategories();
}
