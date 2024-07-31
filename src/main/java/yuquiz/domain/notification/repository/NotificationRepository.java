package yuquiz.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
