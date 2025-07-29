package com.example.LuckyPawsBackend.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime; // 날짜&시간

import com.example.LuckyPawsBackend.dto.ChatMessageDto; // <-- DTO 클래스 임포트
import com.example.LuckyPawsBackend.entity.ChatMessage;
import com.example.LuckyPawsBackend.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component //스프링이 자동관리
public class ChatSocketHandler extends TextWebSocketHandler {
//웹소켓 연결된 사람들목록(sessions)
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ChatMessageRepository chatMessageRepository; // 리포지토리 필드 추가, db저장도구


    // 3. JSON을 자바 객체로, 자바 객체를 JSON으로 바꿔주는 '번역기' (objectMapper)
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 생성자, 의존성 주입해줌
    @Autowired
    public ChatSocketHandler(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // 연결시킨다
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("✅ 클라이언트 연결됨: " + session.getId());
    }

// 메시지(텍스트)를 타인이 보냈을 때 호출됨 . 서버에서 가져옴
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload(); // 받은 JSON 데이터를 가져온다
        System.out.println(">> 서버 수신 페이로드: " + payload); //받은데이터 출력

        try {
            //1. objectmapper로 JSON => dto로
            ChatMessageDto receivedMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
            System.out.println(">> DTO 파싱 성공: 발신자=" + receivedMessageDto.getSender() + ", 메시지=" + receivedMessageDto.getText());

            // 2. DTO를 엔티티로 변환하여 DB에 저장
            ChatMessage chatMessageEntity = new ChatMessage();
            chatMessageEntity.setSender(receivedMessageDto.getSender());
            chatMessageEntity.setMessage(receivedMessageDto.getText());
            chatMessageEntity.setSentAt(LocalDateTime.now()); // 현재 시간으로 설정

            chatMessageEntity.setChatRoomId(1L); // TODO: 실제 채팅방 ID로 설정해야 함

            // 3. 메시지 저장
            ChatMessage savedMessage = chatMessageRepository.save(chatMessageEntity); // DB에 저장하고, ID가 부여된 엔티티를 받음
            System.out.println(">> 메시지 DB에 저장됨: " + savedMessage.getId());


            // 저장된 엔티티의 정보(예: DB에서 부여된 ID나 정확한 sentAt)를 다시 클라이언트에게 보냄
            // 필요하다면 DTO로 변환하여 보내거나, 엔티티를 직접 JSON으로 변환하여 보낼 수 있습니다.
            // 여기서는 DTO로 다시 변환하여 보내는 예시 (클라이언트가 DTO 형식에 맞춰 받음)
            // DTO에 ID와 시간 필드를 추가해야 함.
            ChatMessageDto broadcastDto = new ChatMessageDto(savedMessage.getMessage(), savedMessage.getSender());
//            broadcastDto.setId(savedMessage.getId()); // DTO에 id 필드를 추가했다면
//            broadcastDto.setSentAt(savedMessage.getSentAt()); // DTO에 sentAt 필드를 추가했다면

            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(objectMapper.writeValueAsString(broadcastDto))); // DTO를 보내는 예시
                    // 또는 s.sendMessage(new TextMessage(objectMapper.writeValueAsString(savedMessage))); // 엔티티를 직접 보내는 예시
                } else {
                    System.out.println("❌ 경고: 세션이 닫혀 있음: " + s.getId());
                }
            }

        } catch (IOException e) {
            System.err.println("❌ 오류: 메시지 파싱 또는 브로드캐스트 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);
//
//        for (WebSocketSession s : sessions) {
//            if (s.isOpen()) {  //연결이 끊기지 않도록 추가.
//                try {
//                    s.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(chatMessage)));
//                } catch (IOException e) {
//                    System.out.println("예외발생. ");
//                    e.printStackTrace();
//                }
//            }else {
//                System.out.println("❌ 세션이 닫혀 있음: " + s.getId());
//
//            }
//        }
//    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("🔌 연결 해제: " + session.getId());
    }
}

