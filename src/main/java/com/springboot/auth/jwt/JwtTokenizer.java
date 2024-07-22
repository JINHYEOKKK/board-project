package com.springboot.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenizer {
    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    // 위 access 토큰이 만료가되면 토큰 재발행하여 반환
    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    // 매개변수로 주어진 비밀 키를 Base64 형식으로 인코딩
    // Encoders.BASE64 형식으로 인코딩 하려면 byte 형태로 줘야자 인코드를 할수있음
    // 그래서 문자열 형태의 비밀 키를 byte 배열로 변환하여 Base64 형싱으로 인코딩
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 액세스 토큰을 생성하는 메서드.
    public String generateAccessToken(Map<String, Object> claims, // 토큰에 포함될 추가정보
                                      String subject, // 토큰의 주체, 일반적으로 사용자 식별자: email, Id
                                      Date expiration, // 토큰의 만료시간
                                      String base64EncodedSecretKey) { // 비밀 키를 Base64로 인코딩한 문자열
        // Base64로 인코딩된 비밀 키로부터 Key 객체를 생성.
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        // Jwt 토큰을 생성하여 반환
        return Jwts.builder()
                .setClaims(claims) // 클레임(추가 정보)을 설정.
                .setSubject(subject) // 주체(사용자 식별자 예: email)를 설정.
                .setIssuedAt(Calendar.getInstance().getTime()) // 토큰 발급 시간을 현재 시간으로 설정.
                .setExpiration(expiration) // 만료 시간을 설정.
                .signWith(key) // 서명을 위한 키를 설정.
                .compact(); // 토큰을 문자열로 직렬화하여 반환.

    }

    public String generateRefreshToken(String subject, // 토큰의 주체, 일반적으로 사용자 식별자: email, Id
                                      Date expiration, // 토큰의 만료시간
                                      String base64EncodedSecretKey) { // 비밀 키를 Base64로 인코딩한 문자열
        // Base64로 인코딩된 비밀 키로부터 Key 객체를 생성.
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        // Jwt 토큰을 생성하여 반환
        return Jwts.builder()
                .setSubject(subject) // 주체(사용자 식별자 예: email)를 설정.
                .setIssuedAt(Calendar.getInstance().getTime()) // 토큰 발급 시간을 현재 시간으로 설정.
                .setExpiration(expiration) // 만료 시간을 설정.
                .signWith(key) // 서명을 위한 키를 설정.
                .compact(); // 토큰을 문자열로 직렬화하여 반환.

    }

    /**JWT 구조
     * Header: 토큰의 타입과 해싱 알고리즘 정보를 담고 있습니다.
     * Payload: 실제 클레임(Claims) 정보가 담겨 있는 부분입니다.
     * Signature: 토큰의 무결성을 확인하기 위해 사용됩니다.
     * HEADER.PAYLOAD.SIGNATURE
     */
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);

        return claims;
    }

    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    // 이 메서드는 Base64 로 인코딩된 비밀키를 반대로 디코딩하여 문자열인 Key 타입으로 만든다.
    // Base64 형식의 문자열을 byte 배열로 디코딩
    // Keys.hmacShaKeyFor(keyBytes) 는 byte 배열 형식의 비밀키를 다시 문자열 Key로 변환
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }
}
