package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.response.UserResponseDto;
import com.example.LuckyPawsBackend.entity.User;
import com.example.LuckyPawsBackend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // <-- 이 부분을 완성해야 합니다!
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
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getLoggedInUserInfo(Authentication authentication) {
        // 1. 인증 정보 확인: Spring Security가 자동으로 인증된 사용자 정보를 주입해줍니다.
        if (authentication == null || !authentication.isAuthenticated()) {
            // 이 블록은 Spring Security 설정에 따라 거의 도달하지 않을 수 있지만, 안전을 위해 남겨둡니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        // 2. 로그인된 사용자의 식별자 (email 또는 username) 가져오기
        // authentication.getName()은 UserDetails 구현체의 getUsername() 값을 반환합니다.
        // 일반적으로 이메일 또는 사용자 ID가 됩니다.
        String loggedInUserIdentifier = authentication.getName();

        // 3. UserRepository를 사용하여 데이터베이스에서 User 엔티티 조회
        // UserRepository에 findByEmail(String email) 메서드가 정의되어 있어야 합니다.
        Optional<User> userOptional = userRepository.findByEmail(loggedInUserIdentifier);

        // 4. 사용자 정보가 존재하는지 확인하고 DTO로 변환하여 반환
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto userResponseDto = new UserResponseDto(user); // User 엔티티로 DTO 생성
            return ResponseEntity.ok(userResponseDto); // 200 OK와 함께 사용자 정보 반환
        } else {
            // 이 경우는 인증된 사용자인데 DB에 정보가 없는 매우 드문 비정상적인 상황입니다.
            System.err.println("인증된 사용자(" + loggedInUserIdentifier + ")의 정보를 DB에서 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found 또는 500 Internal Server Error
        }
        }


    // ... (다른 메서드들) ...
}