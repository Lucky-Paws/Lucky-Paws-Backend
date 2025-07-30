package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.response.UserResponseDto;
import com.example.LuckyPawsBackend.entity.User;
import com.example.LuckyPawsBackend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users") // 사용자 관련 API의 기본 경로
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 현재 로그인한 사용자 정보를 반환하는 GET 메서드
    // 이 메서드는 로그인된 사용자만 접근 가능하도록 Spring Security 설정이 필요합니다.
    // GET /api/users/me

    // ... (다른 메서드들) ...
}