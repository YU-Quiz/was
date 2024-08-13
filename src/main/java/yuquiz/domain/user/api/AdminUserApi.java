package yuquiz.domain.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import yuquiz.domain.user.dto.UserSortType;
import yuquiz.domain.user.dto.req.UserStatusReq;

@Tag(name = "[관리자용 사용자 API]", description = "관리자용 사용자 관련 API")
public interface AdminUserApi {

    @Operation(summary = "전체 사용자 페이지별 조회", description = "전체 사용자를 정렬 기준에 따라 페이지별로 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퀴즈 조회 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                             "totalPages": 1,
                             "totalElements": 3,
                             "first": true,
                             "last": true,
                             "size": 20,
                             "content": [
                                 {
                                     "id": 1,
                                     "username": "test",
                                     "nickname": "테스터",
                                     "email": "test@gmail.com",
                                     "createdAt": "2024-08-05T03:25:02.974794"
                                 },
                                 {
                                     "id": 7,
                                     "username": "test111",
                                     "nickname": "테스터111",
                                     "email": "test111@gmail.com",
                                     "createdAt": "2024-08-10T02:13:51.658051"
                                 },
                                 {
                                     "id": 8,
                                     "username": "admin",
                                     "nickname": "admin",
                                     "email": "admin@gmail.com",
                                     "createdAt": "2024-08-10T18:03:12.727292"
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
                             "numberOfElements": 3,
                             "empty": false
                        }
                    """)
            }))
    })
    ResponseEntity<?> getAllUsers(@RequestParam UserSortType sort,
                                  @RequestParam @Min(0) Integer page);

    @Operation(summary = "사용자 강제 탈퇴", description = "사용자id를 기반으로 사용자를 탈퇴시키는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "사용자 탈퇴 성공")
    })
    ResponseEntity<?> deleteUser(@PathVariable Long userId);

    @Operation(summary = "사용자 정지, 사면, 취소", description = "사용자 강제 정지, 정지 사면, 정지 취소 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정지 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "response": "회원 정지 성공"
                        }
                    """)
            })),
            @ApiResponse(responseCode = "200", description = "사용자 사면 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "response": "회원 사면 성공"
                        }
                    """)
            })),
            @ApiResponse(responseCode = "200", description = "사용자 정지 취소 성공",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "response": "회원 정지 취소 성공"
                        }
                    """)
            })),
            @ApiResponse(responseCode = "404", description = "사용자 존재하지 않음",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                        {
                            "status": 404,
                            "message": "존재하지 않는 사용자입니다."
                        }
                    """)
            }))
    })
    ResponseEntity<?> suspendUser(@PathVariable Long userId,
                                  @RequestParam UserStatusReq status);
}
