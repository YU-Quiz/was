package yuquiz.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.notification.dto.NotificationReq;
import yuquiz.domain.notification.dto.NotificationRes;
import yuquiz.domain.notification.dto.NotificationSortType;
import yuquiz.domain.notification.entity.Notification;
import yuquiz.domain.notification.repository.NotificationRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<NotificationRes> getAllNotification(Long userId, Integer page, NotificationSortType sort, Optional<Boolean> isChecked) {
        Pageable pageable = PageRequest.of(page, 20, sort.getSort());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Page<Notification> notifications;

        if (isChecked.isPresent()) {
            notifications = notificationRepository.findAllByUserAndIsChecked(user, isChecked.get(), pageable);
        } else {
            notifications = notificationRepository.findAllByUser(user, pageable);
        }

        return notifications.map(NotificationRes::fromEntity);
    }

    @Transactional
    public void createNotification(Long userId, NotificationReq notificationReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Notification notification = notificationReq.toEntity(user);

        notificationRepository.save(notification);
    }
}
