package yuquiz.domain.report.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import yuquiz.domain.report.dto.ReportReq;

public interface ReportApi {
    @Operation(summary = "퀴즈 신고", description = "퀴즈의 오류 혹은 문제를 신고하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 신고 완료",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "response": "퀴즈 신고 완료."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 퀴즈",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 퀴즈입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "reason": "신고 사유는 필수 입력입니다.",
                                        "type": "신고 유형은 필수 입력입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> reportQuiz(@PathVariable(value = "quizId") Long quizId,
                                 @Valid @RequestBody ReportReq reportReq);
}
