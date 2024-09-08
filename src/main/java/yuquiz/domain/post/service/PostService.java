package yuquiz.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.category.entity.Category;
import yuquiz.domain.category.exception.CategoryExceptionCode;
import yuquiz.domain.category.repository.CategoryRepository;
import yuquiz.domain.likedPost.repository.LikedPostRepository;
import yuquiz.domain.post.dto.PostReq;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.dto.PostSortType;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.repository.PostRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LikedPostRepository likedPostRepository;

    private final Integer POST_PER_PAGE = 20;

    @Transactional
    public void createPost(PostReq postReq, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Category category = categoryRepository.findById(postReq.categoryId())
                .orElseThrow(() -> new CustomException(CategoryExceptionCode.INVALID_ID));

        Post post = postReq.toEntity(user, category);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostRes getPostById(Long postId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        boolean isLiked = likedPostRepository.existsByUserAndPost(user, post);

        post.increaseViewCount();

        return PostRes.fromEntity(post, isLiked);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryRes> getPosts(String keyword, Long categoryId, PostSortType sort, Integer page) {

        Pageable pageable = PageRequest.of(page, POST_PER_PAGE, sort.getSort());

        Page<Post> posts = postRepository.findPostsByKeywordAndCategory(keyword, categoryId, pageable);

        return posts.map(PostSummaryRes::fromEntity);
    }

    @Transactional
    public void updatePostById(Long postId, PostReq postReq, Long userId){

        if(!isOwner(postId, userId)){
            throw new CustomException(PostExceptionCode.UNAUTHORIZED_ACTION);
        }

        Category category = categoryRepository.findById(postReq.categoryId())
                .orElseThrow(() -> new CustomException(CategoryExceptionCode.INVALID_ID));

        postRepository.updateById(postId, postReq.title(), postReq.content(), category);
    }

    @Transactional
    public void deletePostById(Long postId, Long userId){

        if(!isOwner(postId, userId)){
            throw new CustomException(PostExceptionCode.UNAUTHORIZED_ACTION);
        }

        postRepository.deleteById(postId);
    }

    private boolean isOwner(Long postId, Long userId) {
        Optional<Long> writerId = postRepository.findWriterIdById(postId);

        return writerId.isPresent() && writerId.get().equals(userId);
    }
}
