package com.springboot.auth.dto;

import lombok.Getter;

// 로그인 인증 정보 역직렬화(Deserialization)를 위한 LoginDTO 클래스
// 클라이언트로 부터 인증 정보를 수신할 LoginDto
// Json 에서 dto 객체
@Getter
public class LoginDto {
    private String username;
    private String password;
}

