package com.example.LuckyPawsBackend.service;

import com.example.LuckyPawsBackend.dto.request.PostCreateRequestDto;
import com.example.LuckyPawsBackend.dto.response.PostListResponseDto;
import com.example.LuckyPawsBackend.dto.response.PostResponseDto;
import com.example.LuckyPawsBackend.entity.Post;
import com.example.LuckyPawsBackend.entity.Scrap;
import com.example.LuckyPawsBackend.entity.User;
import com.example.LuckyPawsBackend.repository.PostRepository;
import com.example.LuckyPawsBackend.repository.ScrapRepository;
import com.example.LuckyPawsBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;

    /** 게시글 작성 */
    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        post.setCreatedAt(LocalDateTime.now());
        post.setLikes(0);
        post.setScraps(0);

        Post saved = postRepository.save(post);
        return new PostResponseDto(saved);
    }

    /** 게시글 목록 조회 (카테고리 + 페이징) */
    public List<PostListResponseDto> getPosts(String category, int page) {
        ArrayList<PostListResponseDto> responses = new ArrayList<>();

        ArrayList<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            responses.add(new PostListResponseDto(
                    post.getId(),
                    post.getTitle(),
                    post.getCategory(),
                    post.getUser().getNickname(),
                    post.getLikes(),
                    post.getScraps(),
                    post.getCreatedAt()
            ));
        }

        return responses;
    }

    /** 게시글 단건 조회 */
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return new PostResponseDto(post);
    }

    /** 게시글 좋아요 */
    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setLikes(post.getLikes() + 1);
    }

    /** 게시글 스크랩 */
    @Transactional
    public void scrapPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Scrap scrap = new Scrap();
        scrap.setPost(post);
        scrap.setUser(user);
        scrapRepository.save(scrap);

        post.setScraps(post.getScraps() + 1);
    }
}