package yuquiz.domain.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;

    private static final Integer POST_PER_PAGE = 10;

    public Page<PostRes> getLatestPostsByPage(Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, POST_PER_PAGE);
        Page<Post> page = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return page.map(PostRes::from);
    }

    @Transactional
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        postRepository.delete(post);
    }
}
