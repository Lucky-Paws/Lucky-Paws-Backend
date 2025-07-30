package com.example.LuckyPawsBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String authorNickname;
    private boolean anonymous;
    private LocalDateTime createdAt;
}
