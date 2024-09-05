package yuquiz.domain.likedPost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.likedPost.entity.LikedPost;
import yuquiz.domain.likedPost.repository.LikedPostRepository;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.repository.PostRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikedPostService {

    private final LikedPostRepository likedPostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final Integer POST_PER_PAGE = 20;

    @Transactional(readOnly = true)
    public Page<PostSummaryRes> getLikedPosts(Long id, Integer page) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Pageable pageable = PageRequest.of(page, POST_PER_PAGE);

        return likedPostRepository.findAllByUser(user, pageable)
                .map(likedPost -> PostSummaryRes.fromEntity(likedPost.getPost()));
    }

    @Transactional
    public void likePost(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        if(likedPostRepository.existsByUserAndPost(user, post)){
            throw new CustomException(PostExceptionCode.ALREADY_LIKED);
        }

        LikedPost likedPost = LikedPost.builder()
                .user(user)
                .post(post)
                .build();

        likedPostRepository.save(likedPost);
        post.increaseLikeCount();
    }

    @Transactional
    public void deleteLikePost(Long userId, Long postId) {

        likedPostRepository.findByUser_IdAndPost_Id(userId, postId).ifPresent(lp -> {
                lp.getPost().decreaseLikeCount();
                likedPostRepository.delete(lp);
        });
    }
}
