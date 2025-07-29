package com.example.LuckyPawsBackend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private Long postId;  // 단일 게시글 조회나 수정 시 필요
}
