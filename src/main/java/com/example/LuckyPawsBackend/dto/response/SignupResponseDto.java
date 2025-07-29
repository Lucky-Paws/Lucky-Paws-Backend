package com.example.LuckyPawsBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponseDto {
    private Long id;
    private String email;
    private String nickname;
}