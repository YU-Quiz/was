package yuquiz.security.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yuquiz.domain.user.entity.User;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
public class SecurityUserDetails implements UserDetails {

    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUserDetails(User user) {
        this.user = user;
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().getValue()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException();
    }
}
