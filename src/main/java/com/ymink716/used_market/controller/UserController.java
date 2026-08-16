package com.ymink716.used_market.controller;

import com.ymink716.used_market.dto.AddUserRequest;
import com.ymink716.used_market.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> signup(AddUserRequest request) {
        userService.save(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
