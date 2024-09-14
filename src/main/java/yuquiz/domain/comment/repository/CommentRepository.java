package yuquiz.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c.writer.id from Comment c where c.id = :id")
    Optional<Long> findWriterIdById(@Param("id") Long id);

    @Modifying
    @Query("update Comment c set c.content = :content where c.id = :id")
    void updateById(@Param("id") Long id,
                    @Param("content") String content);

    List<Comment> findAllByPost_Id(Long postId);
}
