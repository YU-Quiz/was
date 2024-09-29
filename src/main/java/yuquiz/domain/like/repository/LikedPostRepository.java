package yuquiz.domain.like.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yuquiz.domain.like.entity.LikedPost;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.user.entity.User;

import java.util.Optional;

public interface LikedPostRepository extends JpaRepository<LikedPost, Long> {

    Page<LikedPost> findAllByUser(User user, Pageable pageable);

    boolean existsByUserAndPost(User user, Post post);

    Optional<LikedPost> findByUser_IdAndPost_Id(Long userId, Long postId);
}
