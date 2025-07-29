package com.example.LuckyPawsBackend.repository;

import com.example.LuckyPawsBackend.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
