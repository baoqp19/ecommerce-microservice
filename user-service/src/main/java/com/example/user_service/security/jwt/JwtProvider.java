package com.example.user_service.security.jwt;

import com.example.user_service.entity.User;
import com.example.user_service.security.userprinciple.UserPrinciple;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    // ghi log
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")  // thời gian sống của Token
    private String jwtSecret;
    
    @Value("${jwt.expiration}") // thời gian chết trên hệ thống
    private int jwtExpiration;
    // Key để ký JWT
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 🔹 Tạo Token
    public String createToken(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 🔹 Kiểm tra Token hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("⚠️ Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("⚠️ Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("⚠️ Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("⚠️ JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // 🔹 Lấy Username từ Token
    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}