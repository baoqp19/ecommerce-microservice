package com.example.user_service.service.impl;

import com.example.user_service.dto.model.TokenManager;
import com.example.user_service.dto.request.SignUpForm;
import com.example.user_service.dto.response.JwtResponse;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleName;
import com.example.user_service.entity.User;
import com.example.user_service.repository.IRoleRepository;
import com.example.user_service.repository.IUserRepository;
import com.example.user_service.security.jwt.JwtProvider;
import com.example.user_service.security.userprinciple.UserPrinciple;
import com.example.user_service.service.IUserService;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    private static IUserRepository userRepository;
    private static IRoleRepository roleRepository;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationManager authenticationManager;
    private static JwtProvider jwtProvider;
    private static TokenManager tokenManager;

    public UserServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtProvider jwtProvider,
            TokenManager tokenManager) {
        UserServiceImpl.userRepository = userRepository;
        UserServiceImpl.roleRepository = roleRepository;
        UserServiceImpl.passwordEncoder = passwordEncoder;
        UserServiceImpl.authenticationManager = authenticationManager;
        UserServiceImpl.jwtProvider = jwtProvider;
        UserServiceImpl.tokenManager = tokenManager;
    }

    public Mono<User> registerUser(SignUpForm signUpForm) {
        return Mono.defer(() -> {
            if (existsByUsername(signUpForm.getUsername())) {
                return Mono.error(new RuntimeException(
                        "The username " + signUpForm.getUsername() + " is existed, please try again."));
            }
            if (existsByEmail(signUpForm.getEmail())) {
                return Mono.error(
                        new RuntimeException("The email " + signUpForm.getEmail() + " is existed, please try again."));
            }

            Set<Role> roles = new HashSet<>();

            signUpForm.getRoles().forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository
                                .findByName(RoleName.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role not found."));
                        roles.add(adminRole);
                    }
                    case "pm" -> {
                        Role pmRole = roleRepository
                                .findByName(RoleName.PM)
                                .orElseThrow(() -> new RuntimeException("Role not found."));
                        roles.add(pmRole);
                    }
                    default -> {
                        Role userRole = roleRepository
                                .findByName(RoleName.USER)
                                .orElseThrow(() -> new RuntimeException("Role not found."));
                        roles.add(userRole);
                    }
                }
            });

            User user = User.builder()
                    .name(signUpForm.getName())
                    .username(signUpForm.getUsername())
                    .email(signUpForm.getEmail())
                    .password(passwordEncoder.encode(signUpForm.getPassword()))
                    .avatar("https://www.facebook.com/photo/?fbid=723931439407032&set=pob.100053705482952")
                    .roles(roles)
                    .build();

            userRepository.save(user);

            return Mono.just(user);
        });
    }

    public Mono<JwtResponse> login(SignUpForm signUpFrom) {
        return Mono.defer(() -> {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signUpFrom.getUsername(), signUpFrom.getPassword()));

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            // generate token by authentication
            String token = jwtProvider.createToken(authentication);

            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

            // Lưu trữ tên người dùng và token bằng TokenManager
            tokenManager.storeToken(userPrinciple.getUsername(), token);

            JwtResponse jwtResponse = new JwtResponse(
                    token,
                    userPrinciple.getId(),
                    userPrinciple.getName(),
                    userPrinciple.getAuthorities());

            return Mono.just(jwtResponse);
        });
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
