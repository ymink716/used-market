package com.ymink716.used_market.service;

import com.ymink716.used_market.common.exception.DuplicateUserException;
import com.ymink716.used_market.common.exception.UserNotFoundException;
import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.AddUserRequest;
import com.ymink716.used_market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(AddUserRequest dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateUserException("이미 가입된 이메일입니다.");
        }

        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new DuplicateUserException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .nickname(dto.getNickname())
            .build();

        return userRepository.save(user).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() ->
                new UserNotFoundException("존재하지 않는 사용자입니다.")
            );
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UserNotFoundException("존재하지 않는 사용자입니다.")
            );
    }
}
