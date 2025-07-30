package com.example.LuckyPawsBackend.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /ws/chat/{roomId} 경로를 핸들링하도록 등록
        registry.addHandler(new ChatWebSocketHandler(), "/ws/chat/*")
                .setAllowedOrigins("*"); // 실제 서비스에서는 도메인 제한 권장
    }
}


