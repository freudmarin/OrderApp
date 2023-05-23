package com.marindulja.template.springresttemplate.service;

import com.marindulja.template.springresttemplate.adapters.UserAdapter;
import com.marindulja.template.springresttemplate.dto.LoginRequest;
import com.marindulja.template.springresttemplate.security.JwtProvider;
import com.marindulja.template.springresttemplate.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;

    final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String role = userService.loadUserByUsername(loginRequest.getUsername()).getAuthorities().
                stream().map(grantedAuthority -> StringUtils.remove(grantedAuthority.getAuthority(),"ROLE_")).findFirst().get();
        String authenticationToken = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(authenticationToken, loginRequest.getUsername(), role);
    }

    public Optional<UserAdapter> getCurrentUser() {
        UserAdapter principal = (UserAdapter) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return Optional.of(principal);
    }
}
