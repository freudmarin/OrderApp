package com.marindulja.orderapp.adapters;

import com.marindulja.orderapp.model.User;
import com.marindulja.orderapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserAdapter implements UserDetails {

    private final Long userId;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private final UserRepository userRepository;

    public UserAdapter(Long userId, String password, Collection<? extends GrantedAuthority> authorities,
                       UserRepository userRepository) {
        this.userId = userId;
        this.password = password;
        this.authorities = authorities;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userRepository.findById(Long.valueOf(userId)).get().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        if (user.isDeleted()) {
            return false;
        }
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


