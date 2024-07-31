package yuquiz.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
