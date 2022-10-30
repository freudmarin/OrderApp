package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.LoginRequest;
import com.marindulja.template.springresttemplate.security.JwtProvider;
import com.marindulja.template.springresttemplate.service.AuthService;
import com.marindulja.template.springresttemplate.service.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }
}
