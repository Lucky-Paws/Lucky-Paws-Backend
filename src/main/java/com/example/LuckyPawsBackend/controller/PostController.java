package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.request.PostCreateRequestDto;
import com.example.LuckyPawsBackend.dto.response.PostResponseDto;
import com.example.LuckyPawsBackend.dto.response.PostListResponseDto;
import com.example.LuckyPawsBackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /** 게시글 작성 */
    @PostMapping
    public PostResponseDto createPost(@RequestBody PostCreateRequestDto dto) {
        return postService.createPost(dto);
    }

    /** 게시글 목록 조회 (카테고리 + 페이지) */
    @GetMapping
    public List<PostListResponseDto> getPosts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page
    ) {
        return postService.getPosts(category, page);
    }

    /** 게시글 상세 조회 */
    @GetMapping("/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    /** 게시글 좋아요 */
    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId) {
        postService.likePost(postId);
    }

    /** 게시글 스크랩 */
    @PostMapping("/{postId}/scrap")
    public void scrapPost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        postService.scrapPost(postId, userId);
    }
}