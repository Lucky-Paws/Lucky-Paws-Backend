package com.example.LuckyPawsBackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final List<String> messages = new ArrayList<>();  //메시지만 보내기

    @PostMapping("/send")
    public ResponseEntity<String>sendMessage(@RequestBody String msg) {
        messages.add(msg);
        //messages.add(new ChatMessage(msg,"me"));
        System.out.println(">> 메시지 저장: " + msg);

        return ResponseEntity.ok("📤 메시지 저장됨: " + msg);
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllMessages() {
        return ResponseEntity.ok(messages);
    }
}
