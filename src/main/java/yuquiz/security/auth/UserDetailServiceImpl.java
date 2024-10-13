package yuquiz.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        if (userId == null || userId.equals("")) {
            throw new UsernameNotFoundException(userId);
        }

        User foundUser = userRepository.findById(Long.valueOf(userId)).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + userId));

        return new SecurityUserDetails(foundUser);
    }
}
