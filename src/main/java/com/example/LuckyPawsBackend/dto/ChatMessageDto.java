package com.example.LuckyPawsBackend.dto;

// Lombok 라이브러리를 사용한다면 아래 두 줄을 추가합니다.

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDto {

    // 클라이언트가 보내는 JSON 형식과 일치하도록 필드를 정의합니다.
    // 클라이언트 JavaScript 코드는 { text: msg, sender: sender } 형식을 사용합니다.
    private String message;
    private String sender;
    private String sentAt;

    public ChatMessageDto(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }
}