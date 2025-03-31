package com.hoangtien2k3.inventoryservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
public class JwtValidate {

    private static String SECRET_KEY;

    @Value("${jwt.secret-key}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }


        public static boolean validateToken(String token) {
            if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy SECRET_KEY.");
            }
    
            if (token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            }
    
            try {
                // Tạo khóa bí mật hợp lệ
                Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
                // Giải mã token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    
                // Kiểm tra token có hết hạn không
                return !claims.getExpiration().before(new Date());
    
            } catch (ExpiredJwtException ex) {
                throw new IllegalArgumentException("Token đã hết hạn.");
            } catch (MalformedJwtException ex) {
                throw new IllegalArgumentException("Token không hợp lệ.");
            } catch (SignatureException ex) {
                throw new IllegalArgumentException("Lỗi xác thực token.");
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Lỗi khi kiểm tra token: " + ex.getMessage());
            }
        }
    }

    // public static boolean validateToken(String token) {
    // try {
    // if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
    // throw new IllegalArgumentException("Not found secret key in structure");
    // }
    //
    // // Parse và kiểm tra token bằng khóa bí mật
    // SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    // Jws<Claims> claims = Jwts.parser()
    // .setSigningKey(key)
    // .parseClaimsJws(token);
    //
    // // Kiểm tra xem token có hợp lệ không bằng cách kiểm tra thời gian hết hạn
    // Date expirationDate = claims.getBody().getExpiration();
    // Date currentDate = new Date();
    //
    // if (expirationDate.before(currentDate)) {
    // throw new IllegalArgumentException("Token has expired.");
    // }
    //
    // return true;
    // } catch (ExpiredJwtException ex) {
    // throw new IllegalArgumentException("Token has expired.");
    // } catch (MalformedJwtException ex) {
    // throw new IllegalArgumentException("Invalid token.");
    // } catch (SignatureException ex) {
    // throw new IllegalArgumentException("Token validation error.");
    // } catch (IllegalArgumentException ex) {
    // throw new IllegalArgumentException("Token validation error: " +
    // ex.getMessage());
    // }
    // }
