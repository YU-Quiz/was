package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.user.dto.UserSummaryRes;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private static final Integer USER_PER_PAGE = 10;

    public Page<UserSummaryRes> getUserPage(Integer page) {

        Pageable pageable = PageRequest.of(page, USER_PER_PAGE);
        Page<User> users = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        return users.map(UserSummaryRes::fromEntity);
    }
}
