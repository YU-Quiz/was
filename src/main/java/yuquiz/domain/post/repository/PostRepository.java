package yuquiz.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
