package com.example.user_service.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.request.SignUpFrom;
import com.example.user_service.dto.response.JwtResponse;
import com.example.user_service.dto.response.ResponseMessge;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleName;
import com.example.user_service.entity.User;
import com.example.user_service.security.jwt.JwtProvider;
import com.example.user_service.security.userprinciple.UserPrinciple;
import com.example.user_service.service.impl.RoleServiceImpl;
import com.example.user_service.service.impl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthController(
            UserServiceImpl userService,
            RoleServiceImpl roleService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpFrom signUpFrom) {
        if (userService.existsByUsername(signUpFrom.getUsername())) {
            return new ResponseEntity<>(new ResponseMessge("The username existed, please try again."),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmail(signUpFrom.getEmail())) {
            return new ResponseEntity<>(new ResponseMessge("The email existed, please try again."),
                    HttpStatus.BAD_REQUEST);
        }
        
        User user = User.builder()
                .name(signUpFrom.getName())
                .username(signUpFrom.getUsername())
                .email(signUpFrom.getEmail())
                .password(passwordEncoder.encode(signUpFrom.getPassword()))
                .build();

        Set<String> strRoles = signUpFrom.getRoles();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin": {
                    Role adminRole = roleService
                            .findByName(RoleName.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role not found."));
                    roles.add(adminRole);
                    break;
                }
                case "pm": {
                    Role pmRole = roleService
                            .findByName(RoleName.PM)
                            .orElseThrow(() -> new RuntimeException("Role not found."));
                    roles.add(pmRole);
                    break;
                }
                default: {
                    Role userRole = roleService
                            .findByName(RoleName.USER)
                            .orElseThrow(() -> new RuntimeException("Role not found."));
                    roles.add(userRole);
                    break;
                }
            }
        });

        user.setRoles(roles);
        userService.save(user);

        return new ResponseEntity<>(new ResponseMessge("Create User Successfully."), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignUpFrom signUpFrom) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpFrom.getUsername(), signUpFrom.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate token by authentication
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return ResponseEntity.ok(
                new JwtResponse(token, userPrinciple.getId(), userPrinciple.getName(), userPrinciple.getAuthorities()));
    }

}
