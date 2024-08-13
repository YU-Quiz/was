package yuquiz.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.user.api.UserApi;
import yuquiz.domain.user.dto.req.PasswordUpdateReq;
import yuquiz.domain.user.dto.req.PasswordReq;
import yuquiz.domain.user.dto.req.SignUpReq;
import yuquiz.domain.user.dto.req.UserUpdateReq;
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

    /* 비밀번호 수정 */
    @Override
    @PatchMapping("/my/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordUpdateReq passwordReq,
                                            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        userService.updatePassword(passwordReq, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("비밀번호 수정 성공."));
    }

    /* 비밀번호 확인 */
    @Override
    @PostMapping("/my/verify-password")
    public ResponseEntity<?> verifyPassword(@Valid @RequestBody PasswordReq passwordReq,
                                            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessRes.from(userService.verifyPassword(passwordReq, userDetails.getId())));
    }

    /* 아이디 중복 확인 */
    @Override
    @GetMapping("/verify-username")
    public ResponseEntity<?> verifyUsername(@RequestParam(name = "username") String username) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessRes.from(userService.verifyUsername(username)));
    }

    /* 닉네임 중복 확인 */
    @Override
    @GetMapping("/verify-nickname")
    public ResponseEntity<?> verifyNickname(@RequestParam(name = "nickname") String nickname) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessRes.from(userService.verifyNickname(nickname)));
    }

    /* 사용자 삭제 */
    @Override
    @DeleteMapping("/my")
    public ResponseEntity<?> deleteUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails) {

        userService.deleteUserInfo(userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
