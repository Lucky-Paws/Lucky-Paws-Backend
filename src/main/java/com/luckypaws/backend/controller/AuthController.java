package com.luckypaws.backend.controller;

import com.luckypaws.backend.dto.request.LoginRequestDto;
import com.luckypaws.backend.dto.request.SignupRequestDto;
import com.luckypaws.backend.dto.response.LoginResponseDto;
import com.luckypaws.backend.dto.response.SignupResponseDto;
import com.luckypaws.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
