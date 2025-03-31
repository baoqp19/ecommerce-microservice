package com.example.user_service.service.impl;

import com.example.user_service.dto.model.TokenManager;
import com.example.user_service.dto.request.SignInForm;
import com.example.user_service.dto.request.SignUpForm;
import com.example.user_service.dto.response.JwtResponse;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleName;
import com.example.user_service.entity.User;
import com.example.user_service.repository.IRoleRepository;
import com.example.user_service.repository.IUserRepository;
import com.example.user_service.security.jwt.JwtProvider;
import com.example.user_service.security.userprinciple.UserDetailService;
import com.example.user_service.security.userprinciple.UserPrinciple;
import com.example.user_service.service.IUserService;
import com.example.user_service.service.validate.TokenValidate;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final TokenManager tokenManager;
    private final UserDetailService userDetailsService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${refresh.token.url}") // Đường dẫn endpoint để refresh token
    private String refreshTokenUrl;

    public UserServiceImpl(
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider,
            TokenManager tokenManager,
            UserDetailService userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
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
                switch (role.toLowerCase()) {
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

    @Override
    public Mono<JwtResponse> login(SignInForm signInForm) {
        return Mono.defer(() -> {

            String usernameOrEmail = signInForm.getUsername();

            boolean isEmail = usernameOrEmail.contains("@");

            UserDetails userDetails;
            if (isEmail) {
                userDetails = userDetailsService.loadUserByEmail(usernameOrEmail);
            } else {
                userDetails = userDetailsService.loadUserByUsername(usernameOrEmail);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    signInForm.getPassword(), userDetails.getAuthorities());

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            // generate token by authentication
            // Generate token and refresh token using JwtProvider
            String accessToken = jwtProvider.createToken(authentication);
            String refreshToken = jwtProvider.createRefreshToken(authentication);

            UserPrinciple userPrinciple = (UserPrinciple) userDetails;

            // Store the token and refresh token using TokenManager
            tokenManager.storeToken(userPrinciple.getUsername(), accessToken);
            tokenManager.storeRefreshToken(userPrinciple.getUsername(), refreshToken);

            JwtResponse jwtResponse = new JwtResponse(
                    accessToken,
                    refreshToken,
                    userPrinciple.getId(),
                    userPrinciple.getName(),
                    userPrinciple.getAuthorities());

            return Mono.just(jwtResponse);
        });
    }

    public Mono<String> refreshToken(String refreshToken) {
        return webClientBuilder.build()
                .post()
                .uri(refreshTokenUrl)
                .header("Refresh-Token", refreshToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        clientResponse -> Mono.error(new IllegalArgumentException("Refresh token không hợp lệ")))
                .bodyToMono(JwtResponse.class)
                .map(JwtResponse::getAccessToken); // Sử dụng getAccessToken để lấy token từ JwtResponse
    }

    // validate token -> send RequestHeader
    public static boolean validateToken(String token) {
        return TokenValidate.validateToken(token);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<List<User>> getAllUsers() {
        return Optional.ofNullable(userRepository.findAll());
    }

    // load user by page and size
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

}
