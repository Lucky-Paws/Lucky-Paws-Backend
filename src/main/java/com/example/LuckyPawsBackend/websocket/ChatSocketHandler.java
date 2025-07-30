package com.example.LuckyPawsBackend.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ChatWebSocketHandler extends TextWebSocketHandler {

    // 채팅방 ID 별 WebSocketSession 집합 관리
    private final Map<String, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();

    // 연결시 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = extractRoomId(session);
        if (roomId == null) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        chatRooms.computeIfAbsent(roomId, key -> ConcurrentHashMap.newKeySet()).add(session);
        System.out.println("연결됨: 세션 " + session.getId() + ", 채팅방 " + roomId);
    }

    // 메시지 수신시 호출
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = extractRoomId(session);
        if (roomId == null) return;

        System.out.println("채팅방 " + roomId + "에서 메시지 받음: " + message.getPayload());

        // 같은 채팅방 모든 클라이언트에게 메시지 브로드캐스트
        Set<WebSocketSession> sessions = chatRooms.get(roomId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(message);
                }
            }
        }
    }

    // 연결 종료시 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomId(session);
        if (roomId != null) {
            Set<WebSocketSession> sessions = chatRooms.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    chatRooms.remove(roomId);
                }
            }
        }
        System.out.println("연결 종료: 세션 " + session.getId() + ", 상태 " + status);
    }

    // URI에서 채팅방 ID 추출
    private String extractRoomId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return null;

        // 예: ws://server/ws/chat/1 => path = /ws/chat/1
        String path = uri.getPath();
        String[] parts = path.split("/");
        if (parts.length < 4) return null;  // /ws/chat/{roomId}

        return parts[3];  // {roomId}
    }
}