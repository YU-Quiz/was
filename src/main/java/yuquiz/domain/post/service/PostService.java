package yuquiz.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.category.entity.Category;
import yuquiz.domain.category.exception.CategoryExceptionCode;
import yuquiz.domain.category.repository.CategoryRepository;
import yuquiz.domain.post.dto.PostReq;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.repository.PostRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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
    public PostRes getPostById(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        return PostRes.fromEntity(post);
    }
}
