package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.request.LoginRequestDto;
import com.example.LuckyPawsBackend.dto.request.SignupRequestDto;
import com.example.LuckyPawsBackend.dto.response.LoginResponseDto;
import com.example.LuckyPawsBackend.dto.response.SignupResponseDto;
import com.example.LuckyPawsBackend.service.UserService;
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