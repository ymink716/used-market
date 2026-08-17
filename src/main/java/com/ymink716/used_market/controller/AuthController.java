package com.ymink716.used_market.controller;

import com.ymink716.used_market.dto.*;
import com.ymink716.used_market.service.AuthService;
import com.ymink716.used_market.service.TokenService;
import com.ymink716.used_market.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody AddUserRequest request) {

        userService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse loginResponse = authService.login(request);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CreateAccessTokenResponse> refresh(
        @RequestBody CreateAccessTokenRequest request
    ) {

        String accessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(new CreateAccessTokenResponse(accessToken));
    }
}