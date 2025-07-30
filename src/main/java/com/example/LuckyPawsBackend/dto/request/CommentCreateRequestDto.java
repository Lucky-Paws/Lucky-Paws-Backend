package com.example.LuckyPawsBackend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequestDto {
    private Long userId;      // 댓글 작성자 ID
    private String content;   // 댓글 내용
    private boolean anonymous; // 익명 여부 (true면 닉네임 숨김)
}
