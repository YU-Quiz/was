package yuquiz.domain.report.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

@Tag(name = "[관리자용 신고 API]", description = "관리자용 신고 관련 API")
public interface AdminReportApi {

	@Operation(summary = "전체 신고 페이지별 최신순 조회", description = "전체 신고를 페이지별로 조회하는 API입니다. 신고는 최신순으로 조회됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 조회 성공",
			content = @Content(mediaType = "application/json", examples = {
				@ExampleObject(value = """
					{
						"totalPages": 0,
						"totalElements": 0,
						"first": true,
						"last": true,
						"size": 10,
						"content": [],
						"number": 0,
						"sort": {
							"empty": true,
							"unsorted": true,
							"sorted": false
						},
						"pageable": {
							"pageNumber": 0,
							"pageSize": 10,
							"sort": {
								"empty": true,
								"unsorted": true,
								"sorted": false
							},
							"offset": 0,
							"unpaged": false,
							"paged": true
						},
						"numberOfElements": 0,
						"empty": true
					}
					""")
			}))
	})
	ResponseEntity<?> getReportPage(@RequestParam @Min(0) Integer page);


}
