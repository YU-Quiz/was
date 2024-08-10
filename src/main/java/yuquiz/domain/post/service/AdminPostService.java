package yuquiz.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.post.dto.PostRes;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;

    public static final Integer POST_PER_PAGE = 10;

    public Page<PostRes> getLatestPage(Integer pageNumber){

        Pageable pageable = PageRequest.of(pageNumber, POST_PER_PAGE);
        Page<Post> page = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return page.map(post -> PostRes.from(post));
    }
}
