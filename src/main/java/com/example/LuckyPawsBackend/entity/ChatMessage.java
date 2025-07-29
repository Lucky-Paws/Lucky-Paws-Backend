package com.example.LuckyPawsBackend.entity;

import jakarta.persistence.*; // JPA 관련 어노테이션 임포트
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // 날짜 및 시간 정보를 다루기 위한 임포트

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다. 데이터베이스 테이블과 매핑됩니다.
@Table(name = "chat_message") // 이 엔티티가 매핑될 데이터베이스 테이블의 이름을 명시합니다.
public class ChatMessage {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값이 자동으로 생성됨을 나타냅니다. (MySQL의 AUTO_INCREMENT와 유사)
    private Long id; // 메시지 고유 ID

    // 만약 채팅방 개념이 있다면, 어떤 채팅방에 속하는 메시지인지 구분하는 ID를 가질 수 있습니다.
    // 현재 웹소켓 핸들러에서는 사용하지 않지만, DB 설계에 따라 필요할 수 있습니다.
    private Long chatRoomId;

    private String sender; // 메시지를 보낸 사람의 닉네임 또는 사용자 ID

    @Column(columnDefinition = "TEXT") // 이 필드가 데이터베이스의 TEXT 타입 컬럼에 매핑됨을 명시합니다. (긴 메시지 저장 시 유용)
    private String message; // 실제 메시지 내용 (클라이언트에서 'text'로 보낸 값이 여기에 저장됩니다)

    private LocalDateTime sentAt; // 메시지가 전송된 시간 (Java 8의 날짜/시간 API 사용)

    private Boolean isRead = false; // 메시지 읽음 여부 (기본값은 false, 안 읽음)

    // JPA를 사용하려면 기본 생성자가 필수적입니다.
// (선택 사항) 객체 내용을 쉽게 확인하기 위한 toString() 메서드
    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", chatRoomId=" + chatRoomId +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                ", isRead=" + isRead +
                '}';
    }
}