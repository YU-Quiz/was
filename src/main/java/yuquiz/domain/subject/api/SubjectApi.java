package yuquiz.domain.subject.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "[과목 API]", description = "과목 관련 API")
public interface SubjectApi {
    @Operation(summary = "과목 조회", description = "과목 검색을 활용한 조회 api")
    @ApiResponse(responseCode = "200", description = "과목 조회 성공",
    content = @Content(mediaType = "application/json", examples ={
            @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "subjectName": "운영체제",
                            "subjectCode": "1234"
                        }
                    ]
                    """)
    }))
    ResponseEntity<?> getSubjectByKeyword(
            @RequestParam(value = "keyword", defaultValue = "") String keyword);
}
