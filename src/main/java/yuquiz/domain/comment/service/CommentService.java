package yuquiz.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.comment.dto.CommentReq;
import yuquiz.domain.comment.dto.CommentRes;
import yuquiz.domain.comment.exception.CommentExceptionCode;
import yuquiz.domain.comment.repository.CommentRepository;
import yuquiz.domain.post.entity.Post;
import yuquiz.domain.post.exception.PostExceptionCode;
import yuquiz.domain.post.repository.PostRepository;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(CommentReq commentReq, Long postId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.INVALID_USERID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.INVALID_ID));

        commentRepository.save(commentReq.toEntity(user, post));
    }

    public List<CommentRes> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPost_Id(postId).stream()
                .map(CommentRes::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCommentById(CommentReq commentReq, Long commentId, Long userId) {

        if (!isWriter(commentId, userId)) {
            throw new CustomException(CommentExceptionCode.UNAUTHORIZED_ACTION);
        }

        commentRepository.updateById(commentId, commentReq.content());
    }

    @Transactional
    public void deleteCommentById(Long commentId, Long userId) {

        if (!isWriter(commentId, userId)) {
            throw new CustomException(CommentExceptionCode.UNAUTHORIZED_ACTION);
        }

        commentRepository.deleteById(commentId);
    }

    private boolean isWriter(Long commentId, Long userId) {
        return commentRepository.findWriterIdById(commentId)
                .map(writerId -> writerId.equals(userId))
                .orElse(false);
    }


}
