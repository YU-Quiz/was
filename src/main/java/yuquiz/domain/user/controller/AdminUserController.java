package yuquiz.domain.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.domain.user.api.AdminUserApi;
import yuquiz.domain.user.dto.res.UserSummaryRes;
import yuquiz.domain.user.dto.UserSortType;
import yuquiz.domain.user.dto.req.UserStatusReq;
import yuquiz.domain.user.service.AdminUserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController implements AdminUserApi {

    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam UserSortType sort,
                                         @RequestParam @Min(0) Integer page) {

        Page<UserSummaryRes> users = adminUserService.getAllUsers(sort, page);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    @PatchMapping("/{userId}")
    public ResponseEntity<?> suspendUser(@PathVariable Long userId,
                                         @RequestParam UserStatusReq status){

        adminUserService.updateSuspendStatus(userId, status);

        return switch (status) {
            case SUSPEND -> ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("회원 정지 성공"));
            case UNSUSPEND -> ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("회원 사면 성공"));
            case CANCEL -> ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("회원 정지 취소 성공"));
        };
    }
}
