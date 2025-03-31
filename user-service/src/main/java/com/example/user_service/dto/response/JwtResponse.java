package com.example.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String name;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, String refreshToken, Long id, String name,
            Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = name;
        this.roles = roles;
    }
}
