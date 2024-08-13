package yuquiz.domain.user.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.domain.user.api.AdminUserApi;
import yuquiz.domain.user.dto.UserSortType;
import yuquiz.domain.user.dto.UserSummaryRes;
import yuquiz.domain.user.service.AdminUserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController implements AdminUserApi {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam UserSortType sort,
                                         @RequestParam @Min(0) Integer page) {

        Page<UserSummaryRes> users = adminUserService.getAllUsers(sort, page);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
