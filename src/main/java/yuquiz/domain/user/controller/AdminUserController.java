package yuquiz.domain.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.domain.user.dto.UserSummaryRes;
import yuquiz.domain.user.service.AdminUserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<?> getUserPage(@RequestParam @Min(0) Integer page) {

        Page<UserSummaryRes> users = adminUserService.getUserPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
