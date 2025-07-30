package com.example.LuckyPawsBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateResponseDto {
    private Long id;           // 댓글 ID
    private Long postId;       // 소속 게시글 ID
    private String content;    // 댓글 내용
    private String authorNickname; // 작성자 닉네임
    private String createdAt;  // 작성 시간 (문자열로 변환해서 전달)
}
