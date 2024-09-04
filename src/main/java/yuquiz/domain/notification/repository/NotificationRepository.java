package yuquiz.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.notification.entity.Notification;
import yuquiz.domain.user.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByUserAndIsChecked(User user, boolean isChecked, Pageable pageable);

    Page<Notification> findAllByUser(User user, Pageable pageable);
}
