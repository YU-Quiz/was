package yuquiz.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.user.api.UserApi;
import yuquiz.domain.user.dto.SignUpReq;
import yuquiz.domain.user.dto.UserUpdateReq;
import yuquiz.domain.user.service.UserService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    /* 회원가입 */
    @Override
    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq signUpReq) {

        userService.createUser(signUpReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("회원가입 성공."));
    }

    /* 사용자 정보 불러오기 */
    @Override
    @GetMapping("/my")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userDetails.getId()));
    }

    /* 사용자 정보 업데이트 */
    @Override
    @PutMapping("/my")
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody UserUpdateReq updateReq,
                                            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        userService.updateUserInfo(updateReq, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("회원 정보 수정 성공."));
    }

    /* 사용자 삭제 */
    @Override
    @DeleteMapping("/my")
    public ResponseEntity<?> deleteUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails) {

        userService.deleteUserInfo(userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
