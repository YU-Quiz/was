package yuquiz.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("select p.writer.id from Post p where p.id = :id")
    Optional<Long> findWriterIdById(@Param("id") Long id);

    Page<Post> findByWriter_Id(Long userId, Pageable pageable);
}
