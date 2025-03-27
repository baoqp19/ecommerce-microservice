package com.example.inventory_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Service
public class TokenValidationService {

    @Value("${jwt.secret-key}")
    private String secretKey; // Khóa bí mật đã được cấu hình trong application.properties

    public Mono<Boolean> validateToken(String token) {
        try {
            // Parse và kiểm tra token bằng khóa bí mật
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            // Kiểm tra xem token có hợp lệ không
            return Mono.just(true);
        } catch (Exception e) {
            // Xác thực thất bại
            return Mono.just(false);
        }
    }
}