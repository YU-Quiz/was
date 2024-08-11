package yuquiz.domain.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.dto.UserUpdateReq;
import yuquiz.security.auth.SecurityUserDetails;

@Tag(name = "[사용자 API]", description = "사용자 관련 API")
public interface UserApi {
    @Operation(summary = "회원가입", description = "서비스 최초 이용시 회원가입을 하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "회원가입 성공."
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "notBlank", value = """
                                        {
                                            "username": "아이디는 필수 입력 값입니다.",
                                            "password": "비밀번호는 필수 입력 값입니다.",
                                            "nickname": "닉네임은 필수 입력 값입니다.",
                                            "email": "이메일은 필수 입력 값입니다.",
                                            "majorName": "학과는 필수 선택 값입니다."
                                        }
                                    """),
                            @ExampleObject(name = "patternError", value = """
                                        {
                                            "username": "아이디는 특수문자를 제외한 4~20자리여야 합니다.",
                                            "nickname": "닉네임은 특수문자를 제외한 2~10자리여야 합니다.",
                                            "password": "비밀번호는 8~16자 영문과 숫자를 사용하세요."
                                        }
                                    """)
                    }))
    })
    ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq signUpReq);

    @Operation(summary = "사용자 정보 불러오기", description = "로그인한 사용자의 정보를 불러오는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정보 불러오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "username": "test",
                                            "nickname": "테스터",
                                            "email": "test@naver.com",
                                            "agreeEmail": true,
                                            "majorName": "컴퓨터공학과"
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "유저 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "status": 404,
                                            "message": "존재하지 않는 사용자입니다."
                                        }
                                    """)
                    }))
    })
    ResponseEntity<?> getUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "사용자 정보 업데이트", description = "로그인한 사용자의 정보를 업데이트 하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "회원 정보 수정 성공."
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "유효성검사 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "notBlank", value = """
                                        {
                                            "password": "비밀번호는 필수 입력 값입니다.",
                                            "nickname": "닉네임은 필수 입력 값입니다.",
                                            "majorName": "학과는 필수 선택 값입니다.",
                                            "email": "이메일은 필수 입력 값입니다."
                                        }
                                    """),
                            @ExampleObject(name = "patternError", value = """
                                        {
                                            "nickname": "닉네임은 특수문자를 제외한 2~10자리여야 합니다.",
                                            "password": "비밀번호는 8~16자 영문과 숫자를 사용하세요."
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "유저 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "status": 404,
                                            "message": "존재하지 않는 사용자입니다."
                                        }
                                    """)
                    }))
    })
    ResponseEntity<?> updateUserInfo(@Valid @RequestBody UserUpdateReq updateReq,
                                     @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "사용자 탈퇴", description = "로그인한 사용자가 서비스를 탈퇴하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 탈퇴 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "유저 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "status": 404,
                                            "message": "존재하지 않는 사용자입니다."
                                        }
                                    """)
                    }))
    })
    ResponseEntity<?> deleteUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails);
}
