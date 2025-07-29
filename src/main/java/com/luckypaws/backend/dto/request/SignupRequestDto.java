package com.luckypaws.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private int careerYear;        // 경력 연차 (드롭다운 입력)
    private String schoolLevel;    // "초등", "중등", "고등" 중 하나
}
