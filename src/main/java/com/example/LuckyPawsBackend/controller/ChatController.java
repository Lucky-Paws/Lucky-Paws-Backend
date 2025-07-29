package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.ChatMessageDto; // ChatMessageDto 임포트
import com.example.LuckyPawsBackend.entity.ChatMessage; // ChatMessage 엔티티 임포트
import com.example.LuckyPawsBackend.repository.ChatMessageRepository; // ChatMessageRepository 임포트

import org.springframework.http.HttpStatus; // HTTP 상태 코드 사용을 위한 임포트
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // 날짜 포맷팅을 위한 임포트
import java.util.List;
import java.util.stream.Collectors; // Stream API 사용을 위한 임포트

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;

    // 생성자 주입
    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // 새 메시지를 받아서 DB에 저장하고, 저장된 메시지 정보를 반환
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody ChatMessageDto chatMessageDto) {
        // 1. DTO를 Entity로 변환하여 DB에 저장할 준비
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatMessageDto.getChatRoomId()); // <-- DTO에서 chatRoomId 가져와 설정
        chatMessage.setSender(chatMessageDto.getSender());
        chatMessage.setMessage(chatMessageDto.getMessage());
        chatMessage.setSentAt(LocalDateTime.now()); // 현재 시간으로 설정
        chatMessage.setIsRead(false); // 기본값 false 설정 (Boolean 타입에 맞게)

        // 2. 메시지를 DB에 저장
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        System.out.println(">> 메시지 저장: " + savedMessage.getMessage() + " by " + savedMessage.getSender());

        // 3. 저장된 Entity를 다시 DTO로 변환하여 클라이언트에게 응답
        // 날짜 포맷은 클라이언트의 요구사항에 맞춰 조정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ChatMessageDto responseDto = new ChatMessageDto(
                savedMessage.getSender(),
                savedMessage.getMessage(),
                savedMessage.getSentAt().format(formatter),
                savedMessage.getChatRoomId() // 응답에도 chatRoomId 포함
        );

        // 201 Created 상태 코드와 함께 저장된 메시지 DTO 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 특정 채팅방의 메시지를 조회하는 엔드포인트
// 예: GET /api/chat/room/1/messages  (채팅방 ID가 1인 방의 메시지)
    @GetMapping("/room/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByRoom(@PathVariable Long chatRoomId) {
        // ChatMessageRepository에 findByChatRoomIdOrderBySentAtAsc 같은 메서드를 추가해야 함
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<ChatMessageDto> dtos = messages.stream()
                .map(message -> new ChatMessageDto(
                        message.getMessage(),
                        message.getSender(),
                        message.getSentAt().format(formatter),
                        message.getChatRoomId() // DTO에도 chatRoomId 추가
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}