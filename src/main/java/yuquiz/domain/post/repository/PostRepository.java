package yuquiz.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.category.entity.Category;
import yuquiz.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByTitleContainingOrContentContaining(String keyword1, String keyword2, Pageable pageable);

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    @Query("select p.writer.id from Post p where p.id = :id")
    Optional<Long> findWriterIdById(@Param("id") Long id);

    @Modifying
    @Query("update Post p set p.title = :title, p.content = :content, p.category = :category where p.id = :id")
    void updateById(@Param("id") Long id,
                    @Param("title") String title,
                    @Param("content") String content,
                    @Param("category") Category category);
}
