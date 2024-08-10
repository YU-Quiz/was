package yuquiz.security.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class CustomGrantedAuthority implements GrantedAuthority {

    private final String role;

    @JsonCreator
    public CustomGrantedAuthority(@JsonProperty("authority") String role) {
        Assert.hasText(role, "role must not be empty");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

}
