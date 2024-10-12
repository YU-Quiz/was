package yuquiz.domain.study.api;

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
import yuquiz.domain.study.dto.StudyReq;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[스터디 API]", description = "스터디 관련 API")
public interface StudyApi {
    @Operation(summary = "스터디 생성", description = "스터디 생성 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스터디 생성 성공",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                            {
                                "response": "성공적으로 생성되었습니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name= "필수 입력 사항 누락",value = """
                            {
                                "name": "제목은 필수 입력입니다.",
                                "description": "설명은 필수 입력입니다.",
                                "maxUser": "최대 인원은 필수 입력입니다."
                            }
                            """),
                    @ExampleObject(name = "최대 인원 최소 2명", value = """
                            {
                                "maxUser": "최소 인원은 2명입니다."
                            }
                            """)
            }))
    })
    ResponseEntity<?> createStudy(@Valid @RequestBody StudyReq studyReq,
                                  @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "스터디 삭제", description = "스터디 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "퀴즈 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "작성자 불일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    {
                                        "status": 403,
                                        "message": "권한이 없습니다."
                                    }
                                    """)
                    }))
    })
    ResponseEntity<?> deleteStudy(@PathVariable(value = "studyId") Long studyId,
                                  @AuthenticationPrincipal SecurityUserDetails userDetails);
}
