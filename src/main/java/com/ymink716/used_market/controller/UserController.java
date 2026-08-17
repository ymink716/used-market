package com.ymink716.used_market.controller;

import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.AddUserRequest;
import com.ymink716.used_market.dto.UserResponse;
import com.ymink716.used_market.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(Authentication authentication) {

        String email = authentication.getName();
        UserResponse userResponse = new UserResponse(userService.findByEmail(email));

        return ResponseEntity.ok(userResponse);
    }
}
