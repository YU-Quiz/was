package yuquiz.domain.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.post.dto.PostSummaryRes;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;

    private static final Integer POST_PER_PAGE = 10;

    public Page<PostSummaryRes> getLatestPostsByPage(Integer page) {

        Pageable pageable = PageRequest.of(page, POST_PER_PAGE);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return posts.map(PostSummaryRes::fromEntity);
    }

    @Transactional
    public void deletePost(Long postId) {

        postRepository.deleteById(postId);
    }
}
