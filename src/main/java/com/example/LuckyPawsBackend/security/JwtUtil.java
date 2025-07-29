package com.example.LuckyPawsBackend.security;

import io.jsonwebtoken  .Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // 비밀키 (HS256)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 만료 시간: 1시간 (1000ms * 60 * 60)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // JWT 생성
    public static String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}
