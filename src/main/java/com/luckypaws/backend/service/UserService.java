package com.luckypaws.backend.service;

import com.luckypaws.backend.dto.request.SignupRequestDto;
import com.luckypaws.backend.dto.request.LoginRequestDto;
import com.luckypaws.backend.dto.response.SignupResponseDto;
import com.luckypaws.backend.dto.response.LoginResponseDto;
import com.luckypaws.backend.entity.User;
import com.luckypaws.backend.repository.UserRepository;
import com.luckypaws.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SignupResponseDto signup(SignupRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // ⚠ 실제론 비밀번호 암호화 해야 함
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setCareerYear(request.getCareerYear());
        user.setSchoolLevel(request.getSchoolLevel());

        User savedUser = userRepository.save(user);
        return new SignupResponseDto(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname());
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(request.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        User user = optionalUser.get();
        String token = JwtUtil.generateToken(user.getId(), user.getEmail());

        return new LoginResponseDto(user.getId(), user.getEmail(), user.getNickname(), token);
    }
}