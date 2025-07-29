// src/main/java/com/example/LuckyPawsBackend/repository/ChatMessageRepository.java (새로운 repository 패키지 생성)
package com.example.LuckyPawsBackend.repository;

import com.example.LuckyPawsBackend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
}