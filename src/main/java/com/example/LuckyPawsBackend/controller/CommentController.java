package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.request.CommentCreateRequestDto;
import com.example.LuckyPawsBackend.dto.response.CommentResponseDto;
import com.example.LuckyPawsBackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /** 댓글 작성 */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.createComment(postId, dto));
    }

    /** 특정 게시글의 댓글 목록 조회 */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}
