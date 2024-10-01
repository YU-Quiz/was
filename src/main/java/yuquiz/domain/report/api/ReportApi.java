package yuquiz.domain.report.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import yuquiz.domain.report.dto.ReportReq;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[신고 API]", description = "신고 관련 API")
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
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "존재하지 않는 사용자입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "type": "신고 유형은 필수 입력입니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "409", description = "중복 신고",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 409,
                                        "message": "이미 신고한 퀴즈입니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> reportQuiz(@PathVariable(value = "quizId") Long quizId,
                                 @Valid @RequestBody ReportReq reportReq,
                                 @AuthenticationPrincipal SecurityUserDetails userDetails);
}
