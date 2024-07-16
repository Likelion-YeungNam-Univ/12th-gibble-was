package gible.domain.security.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private boolean accountNonLocked;
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean credentialsNonExpired;

    @Builder
    private SecurityUserDetails(Long userId, String username, Collection<? extends GrantedAuthority> authorities, boolean accountNonLocked) {
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
        this.accountNonLocked = accountNonLocked;
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
        return username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }


}
