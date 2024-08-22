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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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
    public PostRes getPostById(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        return PostRes.fromEntity(post);
    }

    @Transactional
    public void updatePost(Long postId, PostReq postReq, Long userId){

        Post post = findByPostIdAndValidateUser(postId, userId);

        Category category;
        if(!post.getCategory().getId().equals(postReq.categoryId())){
            category = categoryRepository.findById(postReq.categoryId())
                    .orElseThrow(() -> new CustomException(CategoryExceptionCode.INVALID_ID));
        }else{
            category = post.getCategory();
        }

        post.update(postReq.title(), postReq.content(), category);
    }

    @Transactional
    public void deletePost(Long postId, Long userId){

        try{
            Post post = findByPostIdAndValidateUser(postId, userId);
            postRepository.delete(post);
        }catch(CustomException e){
            if(!e.getExceptionCode().equals(PostExceptionCode.INVALID_ID)){
                throw e;
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryRes> getPostsByKeyword(String keyword, PostSortType sortType, Integer page) {

        Pageable pageable = PageRequest.of(page, POST_PER_PAGE, sortType.getSort());
        Page<Post> posts =  postRepository.findAllByTitleContainingOrContentContaining(keyword, keyword, pageable);

        return posts.map(PostSummaryRes::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryRes> getPostsByCategory(String categoryName, PostSortType sortType, Integer page){

        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CustomException(CategoryExceptionCode.INVALID_NAME));

        Pageable pageable = PageRequest.of(page, POST_PER_PAGE, sortType.getSort());
        Page<Post> posts = postRepository.findAllByCategory(category, pageable);

        return posts.map(PostSummaryRes::fromEntity);
    }

    private Post findByPostIdAndValidateUser(Long postId, Long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        if(!post.getWriter().getId().equals(userId)){
            throw new CustomException(PostExceptionCode.UNAUTHORIZED_ACTION);
        }

        return post;
    }
}
