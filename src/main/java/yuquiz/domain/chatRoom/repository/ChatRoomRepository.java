package yuquiz.domain.chatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.chatRoom.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
