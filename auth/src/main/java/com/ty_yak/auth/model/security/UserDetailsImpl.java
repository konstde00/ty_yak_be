package com.ty_yak.auth.model.security;

import com.ty_yak.auth.model.entity.User;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserDetailsImpl implements UserDetails {

    String email;
    String password;
    List<GrantedAuthority> roles;

    public UserDetailsImpl(User user) {
        this.email = user.getEmail();
        this.roles = getUserRoles(user);
        this.password = user.getPassword();
    }

    private List<GrantedAuthority> getUserRoles(User user) {
        return user.getRoles().stream().map(Enum::toString).map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
