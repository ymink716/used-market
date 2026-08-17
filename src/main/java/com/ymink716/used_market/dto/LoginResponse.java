package com.ymink716.used_market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
}