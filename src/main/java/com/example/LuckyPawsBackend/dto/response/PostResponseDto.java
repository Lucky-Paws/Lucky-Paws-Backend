package com.example.LuckyPawsBackend.dto.response;

import com.example.LuckyPawsBackend.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String authorNickname;
    private int likes;
    private int scraps;
    private LocalDateTime createdAt;

    // <-- 단일 Post 엔티티만 받도록 생성자 변경
    public PostResponseDto(Post post) {
        this.id               = post.getId();
        this.title            = post.getTitle();
        this.content          = post.getContent();
        this.category         = post.getCategory();
        this.authorNickname   = post.getUser().getNickname();
        this.likes            = post.getLikes();
        this.scraps           = post.getScraps();
        this.createdAt        = post.getCreatedAt();
    }
}
