package com.example.LuckyPawsBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResponseDto {
    private Long id;            // 게시글 ID
    private String title;       // 제목
    private String content;     // 내용
    private String category;    // 카테고리 (예: 초등, 중등, 고등)
    private String authorNickname; // 작성자 닉네임
    private int likes;          // 좋아요 수
    private int scraps;         // 스크랩 수
    private String createdAt;   // 작성 시간 (문자열 변환)
}
