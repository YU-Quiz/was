package yuquiz.domain.chatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.chatRoom.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByStudy_Id(Long studyId);
}
