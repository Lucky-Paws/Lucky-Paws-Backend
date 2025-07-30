package com.example.LuckyPawsBackend.repository;

import com.example.LuckyPawsBackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 카테고리별 페이징 조회
    Page<Post> findByCategory(String category, Pageable pageable);

    // 전체 페이징 조회
    ArrayList<Post> findAll();
}