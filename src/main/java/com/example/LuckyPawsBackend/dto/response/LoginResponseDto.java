package com.example.LuckyPawsBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String token; // JWT 토큰
}