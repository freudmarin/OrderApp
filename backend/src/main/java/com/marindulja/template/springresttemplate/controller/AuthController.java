package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.LoginRequest;
import com.marindulja.template.springresttemplate.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }
}
