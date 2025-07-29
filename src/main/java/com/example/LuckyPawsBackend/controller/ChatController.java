package com.example.LuckyPawsBackend.controller;

import com.example.LuckyPawsBackend.dto.ChatMessageDto; // ChatMessageDto 임포트
import com.example.LuckyPawsBackend.entity.ChatMessage; // ChatMessage 엔티티 임포트
import com.example.LuckyPawsBackend.repository.ChatMessageRepository; // ChatMessageRepository 임포트

import org.springframework.http.HttpStatus; // HTTP 상태 코드 사용을 위한 임포트
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        chatMessage.setChatRoomId(1L); // TODO: 실제 채팅방 ID를 동적으로 받아오도록 수정 필요
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
                savedMessage.getSentAt().format(formatter)
        );

        // 201 Created 상태 코드와 함께 저장된 메시지 DTO 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 모든 채팅 메시지를 DB에서 조회하여 반환
    @GetMapping("/all")
    public ResponseEntity<List<ChatMessageDto>> getAllMessages() {
        // 1. DB에서 모든 ChatMessage 엔티티를 조회
        List<ChatMessage> messages = chatMessageRepository.findAll();

        // 2. 조회된 ChatMessage 엔티티 리스트를 ChatMessageDto 리스트로 변환
        // 이때 LocalDateTime을 클라이언트가 이해할 수 있는 String 형태로 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<ChatMessageDto> dtos = messages.stream()
                .map(message -> new ChatMessageDto(
                        message.getSender(), // ChatMessageDto 생성자 순서에 맞게 sender, message, sentAt
                        message.getMessage(),
                        message.getSentAt().format(formatter)
                ))
                .collect(Collectors.toList());

        // 3. 200 OK 상태 코드와 함께 DTO 리스트 반환
        // Spring Boot가 이 List<ChatMessageDto>를 자동으로 올바른 JSON 배열로 직렬화합니다.
        return ResponseEntity.ok(dtos);
    }
}