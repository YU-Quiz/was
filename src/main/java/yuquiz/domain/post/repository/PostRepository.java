package yuquiz.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.category.entity.Category;
import yuquiz.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByTitleContainingOrContentContaining(String keyword1, String keyword2, Pageable pageable);

    Page<Post> findAllByCategory(Category category, Pageable pageable);
}
