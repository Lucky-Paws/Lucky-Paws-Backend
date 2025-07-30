package com.example.LuckyPawsBackend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostListRequestDto {
    private String category;  // 카테고리 필터 (예: "초등", "중등", "고등")
    private int page;         // 페이지 번호
}
