package com.example.LuckyPawsBackend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {
    private Long userId;     // 작성자 ID
    private String title;    // 제목
    private String content;  // 내용
    private String category; // 카테고리 (예: 초등, 중등, 고등)
}
