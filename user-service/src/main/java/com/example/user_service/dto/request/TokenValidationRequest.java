package com.example.user_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidationRequest {

    private String accessToken;

    public TokenValidationRequest() {
    }

    public TokenValidationRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}