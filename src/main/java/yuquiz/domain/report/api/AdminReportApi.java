package yuquiz.domain.report.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import yuquiz.domain.report.dto.ReportSortType;

@Tag(name = "[관리자용 신고 API]", description = "관리자용 신고 관련 API")
public interface AdminReportApi {

	@Operation(summary = "전체 신고 페이지별 조회", description = "전체 신고를 정렬 기준에 따라 페이지별로 조회하는 API")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 조회 성공",
			content = @Content(mediaType = "application/json", examples = {
				@ExampleObject(value = """
						{
							 "totalPages": 1,
							 "totalElements": 2,
							 "first": true,
							 "last": true,
							 "size": 20,
							 "content": [
								 {
									 "reportId": 4,
									 "quizId": 7,
									 "reason": "재미없음",
									 "type": "REPORT"
								 },
								 {
									 "reportId": 5,
									 "quizId": 7,
									 "reason": "별로에요",
									 "type": "REPORT"
								 }
							 ],
							 "number": 0,
							 "sort": {
								 "empty": false,
								 "unsorted": false,
								 "sorted": true
							 },
							 "pageable": {
								 "pageNumber": 0,
								 "pageSize": 20,
								 "sort": {
									 "empty": false,
									 "unsorted": false,
									 "sorted": true
								 },
								 "offset": 0,
								 "unpaged": false,
								 "paged": true
							 },
							 "numberOfElements": 2,
							 "empty": false
					 	}
					""")
			}))
	})
	ResponseEntity<?> getAllReports(@RequestParam ReportSortType sort,
									@RequestParam @Min(0) Integer page);


}
