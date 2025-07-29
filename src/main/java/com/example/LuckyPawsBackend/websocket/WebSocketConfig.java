package com.example.LuckyPawsBackend.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 기존 엔드포인트
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*") // 개발 중에는 모든 Origin 허용 (운영 시에는 특정 도메인만 허용)
                .withSockJS();

        // ⭐⭐ 추가 또는 수정: Path Variable을 포함하는 웹소켓 엔드포인트도 허용 ⭐⭐
        // 이렇게 하면 프론트에서 ws://localhost:50023/ws/chat/1 로 직접 연결할 수 있습니다.
        registry.addEndpoint("/ws/chat/{chatRoomId}") // {chatRoomId} 추가
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 구독 (subscribe) 접두사
        config.setApplicationDestinationPrefixes("/app"); // 메시지 발행 (publish) 접두사
    }
}


//package com.example.LuckyPawsBackend.websocket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final ChatSocketHandler chatSocketHandler;
//
//    public WebSocketConfig(ChatSocketHandler chatSocketHandler) {
//        this.chatSocketHandler = chatSocketHandler;
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(chatSocketHandler, "/ws/chat{chatRoomId}").setAllowedOrigins("*"); //CORS, 필요에 따라 특정 Origin으로 제한
//    }
//}
