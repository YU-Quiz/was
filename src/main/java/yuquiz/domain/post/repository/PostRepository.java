package yuquiz.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p.writer.id from Post p where p.id = :id")
    Optional<Long> findWriterIdById(@Param("id") Long id);

    @Query("SELECT p FROM Post p WHERE"
            + "(:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%)"
            + "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Post> findPostsByKeywordAndCategory(@Param("keyword") String keyword,
                                             @Param("categoryId") Long categoryId,
                                             Pageable pageable);
}
