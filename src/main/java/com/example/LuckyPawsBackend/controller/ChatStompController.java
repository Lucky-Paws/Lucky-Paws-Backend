package com.example.LuckyPawsBackend.controller; // Controller 패키지에 두는 것이 일반적입니다.

import com.example.LuckyPawsBackend.dto.ChatMessageDto;
import com.example.LuckyPawsBackend.entity.ChatMessage;
import com.example.LuckyPawsBackend.repository.ChatMessageRepository;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations; // 메시지 브로드캐스트용
import org.springframework.stereotype.Controller; // @Controller 사용
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller // ⭐⭐ @RestController가 아닌 @Controller 사용 (STOMP 메시지 처리) ⭐⭐
public class ChatStompController { // 클래스 이름 변경

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations messagingTemplate; // STOMP 메시지 브로드캐스트 도구

    // 생성자 주입
    public ChatStompController(ChatMessageRepository chatMessageRepository, SimpMessageSendingOperations messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // ⭐⭐ 클라이언트가 /app/chat.sendMessage/{chatRoomId} 로 메시지를 보낼 때 이 메서드가 호출됨 ⭐⭐
    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable Long chatRoomId, // URL 경로 변수에서 채팅방 ID 추출
            @Payload ChatMessageDto chatMessageDto // JSON 메시지 본문을 DTO로 자동 변환
    ) {
        System.out.println(">> STOMP 메시지 수신: 채팅방 ID=" + chatRoomId + ", 발신자=" + chatMessageDto.getSender() + ", 메시지=" + chatMessageDto.getMessage());

        // 메시지 유효성 검사
        if (chatMessageDto.getSender() == null || chatMessageDto.getSender().trim().isEmpty() ||
                chatMessageDto.getMessage() == null || chatMessageDto.getMessage().trim().isEmpty()) {
            System.err.println("오류: 발신자 또는 메시지 내용이 비어있습니다. DTO: " + chatMessageDto);
            return;
        }

        // DTO를 Entity로 변환하여 DB에 저장
        ChatMessage chatMessageEntity = new ChatMessage();
        chatMessageEntity.setChatRoomId(chatRoomId); // ⭐⭐ 동적으로 받은 채팅방 ID 설정 ⭐⭐
        chatMessageEntity.setSender(chatMessageDto.getSender());
        chatMessageEntity.setMessage(chatMessageDto.getMessage());
        chatMessageEntity.setSentAt(LocalDateTime.now()); // 서버에서 시간 설정
        chatMessageEntity.setIsRead(false); // 기본값 false 설정

        // 메시지 저장
        ChatMessage savedMessage = chatMessageRepository.save(chatMessageEntity);
        System.out.println(">> 메시지 DB에 저장됨: ID=" + savedMessage.getId() + ", 채팅방 ID=" + savedMessage.getChatRoomId());

        // 저장된 엔티티를 DTO로 변환하여 브로드캐스트 준비
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ChatMessageDto broadcastDto = new ChatMessageDto(
                savedMessage.getSender(),
                savedMessage.getMessage(),
                savedMessage.getSentAt().format(formatter), // 포맷된 시간 문자열
                savedMessage.getChatRoomId()
        );

        // ⭐⭐ 해당 채팅방의 구독자들에게 메시지 브로드캐스트 ⭐⭐
        // 클라이언트는 /topic/room/{chatRoomId}를 구독합니다.
        messagingTemplate.convertAndSend("/topic/room/" + chatRoomId, broadcastDto);
        System.out.println(">>> STOMP 메시지 브로드캐스트 완료 - 채팅방: " + chatRoomId);
    }
}