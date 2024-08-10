package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yuquiz.domain.user.dto.UserRes;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private static final Integer USER_PER_PAGE = 10;

    public Page<UserRes> getUserPage(Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, USER_PER_PAGE);
        Page<User> page = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        return page.map(UserRes::from);
    }
}
