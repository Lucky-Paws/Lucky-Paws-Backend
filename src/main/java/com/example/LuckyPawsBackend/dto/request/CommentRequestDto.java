package com.example.LuckyPawsBackend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long commentId;   // 댓글 ID (조회나 수정 시 필요)
    private Long postId;      // 어떤 게시글의 댓글인지
}
