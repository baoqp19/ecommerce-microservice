package com.example.user_service.dto.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class TokenManager {

    public String TOKEN;
    public String REFRESHTOKEN;
    private Map<String, String> tokenStore = new HashMap<>();
    private Map<String, String> refreshTokenStore = new HashMap<>();

    // Lưu trữ token cho một tên người dùng
    public void storeToken(String username, String token) {
        tokenStore.put(username, token);
        TOKEN = token;
    }

    // Lấy token dựa trên tên người dùng
    public String getTokenByUsername(String username) {
        return tokenStore.get(username);
    }

    // Xóa token dựa trên tên người dùng (ví dụ: khi đăng xuất)
    public void removeToken(String username) {
        tokenStore.remove(username);
    }

    public void storeRefreshToken(String username, String refreshToken) {
        refreshTokenStore.put(username, refreshToken);
        REFRESHTOKEN = refreshToken;
    }
}