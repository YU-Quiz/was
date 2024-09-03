package yuquiz.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yuquiz.domain.notification.dto.DisplayType;
import yuquiz.domain.notification.dto.NotificationRes;
import yuquiz.domain.notification.dto.NotificationSortType;
import yuquiz.domain.notification.service.NotificationService;
import yuquiz.security.auth.SecurityUserDetails;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/my/alert")
    public ResponseEntity<?> getAllMyAlert(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                           @RequestParam(value = "page") Integer page,
                                           @RequestParam(value = "sort") NotificationSortType sort,
                                           @RequestParam(value = "view")DisplayType displayType) {

        Page<NotificationRes> notifications = notificationService.getAllNotification(userDetails.getId(), page, sort, displayType);

        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }
}
