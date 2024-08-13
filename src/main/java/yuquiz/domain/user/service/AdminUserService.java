package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.user.dto.res.UserSummaryRes;
import yuquiz.domain.user.dto.UserSortType;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private static final Integer USER_PER_PAGE = 20;

    public Page<UserSummaryRes> getAllUsers(UserSortType sort, Integer page) {

        Pageable pageable = PageRequest.of(page, USER_PER_PAGE, sort.getSort());
        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserSummaryRes::fromEntity);
    }
}
