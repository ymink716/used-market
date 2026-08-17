package com.ymink716.used_market.service;

import com.ymink716.used_market.config.jwt.TokenProvider;
import com.ymink716.used_market.domain.RefreshToken;
import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.LoginRequest;
import com.ymink716.used_market.dto.LoginResponse;
import com.ymink716.used_market.repository.RefreshTokenRepository;
import com.ymink716.used_market.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword()
        )) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken =
            tokenProvider.generateToken(
                user,
                Duration.ofHours(2)
            );

        String refreshToken =
            tokenProvider.generateToken(
                user,
                Duration.ofDays(14)
            );

        saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponse(
            accessToken,
            refreshToken
        );
    }

    private void saveRefreshToken(Long userId, String refreshToken) {

        RefreshToken token =
            refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(refreshToken))
                .orElseGet(() -> new RefreshToken(userId, refreshToken));

        refreshTokenRepository.save(token);
    }
}