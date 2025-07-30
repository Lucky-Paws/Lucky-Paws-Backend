package com.example.LuckyPawsBackend.service;

import com.example.LuckyPawsBackend.dto.request.CommentCreateRequestDto;
import com.example.LuckyPawsBackend.dto.response.CommentResponseDto;
import com.example.LuckyPawsBackend.entity.Comment;
import com.example.LuckyPawsBackend.entity.Post;
import com.example.LuckyPawsBackend.entity.User;
import com.example.LuckyPawsBackend.repository.CommentRepository;
import com.example.LuckyPawsBackend.repository.PostRepository;
import com.example.LuckyPawsBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /** 댓글 작성 */
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(dto.getContent());
        comment.setAnonymous(dto.isAnonymous());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return new CommentResponseDto(
                saved.getId(),
                saved.getContent(),
                saved.isAnonymous() ? "익명" : saved.getUser().getNickname(),
                saved.isAnonymous(),
                saved.getCreatedAt()
        );
    }

    /** 특정 게시글의 댓글 조회 */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getContent(),
                        c.isAnonymous() ? "익명" : c.getUser().getNickname(),
                        c.isAnonymous(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
