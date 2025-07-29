package com.example.LuckyPawsBackend.dto.response;

import com.example.LuckyPawsBackend.entity.User; // User 엔티티 임포트
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 여기서는 수동으로 생성자를 만듭니다.
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private Integer careerYear; // careerYear가 Integer 타입이라면
    private String schoolLevel;

    // User 엔티티를 받아서 DTO를 생성하는 생성자
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.careerYear = user.getCareerYear();
        this.schoolLevel = user.getSchoolLevel();
        // 비밀번호 필드는 포함하지 않습니다.
    }


    // 필요하다면 Setters도 추가할 수 있습니다 (예: @Data 어노테이션 사용 시)
    // public void setId(Long id) { this.id = id; }
    // ...
}