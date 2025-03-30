package com.example.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.request.SignInForm;
import com.example.user_service.entity.User;
import com.example.user_service.http.HeaderGenerator;
import com.example.user_service.repository.IUserRepository;
import com.example.user_service.security.jwt.JwtProvider;
import com.example.user_service.service.impl.UserServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("api/admin")
public class AdminUserController {

    private final IUserRepository userRepository;
    private final HeaderGenerator headerGenerator;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AdminUserController(IUserRepository userRepository, HeaderGenerator headerGenerator, JwtProvider jwtProvider,
            AuthenticationManager authenticationManager, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.headerGenerator = headerGenerator;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping(value = "/user/{userName}")
    public ResponseEntity<?> getAllUser(@PathVariable("userName") String username) {

        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(null,
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/all")
    public ResponseEntity<?> getAllUsers() {

        List<User> listUsers = userService.getAllUsers()
                .orElseThrow(() -> new UsernameNotFoundException("Not Found List User"));

        return new ResponseEntity<>(listUsers,
                headerGenerator.getHeadersForSuccessGetMethod(),
                HttpStatus.OK);
    }

    @GetMapping("/generate/token")
    public ResponseEntity<String> getToken(@RequestBody SignInForm signInForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));

        // Đoạn mã ở đây để lấy token từ hệ thống xác thực (nếu cần)
        String token = jwtProvider.createToken(authentication);

        // Trả về token trong phản hồi
        return ResponseEntity.ok(token);
    }

    @GetMapping("/user/page")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<User> users = userService.getAllUsers(page, size);
        return new ResponseEntity<>(users,
                headerGenerator.getHeadersForSuccessGetMethod(),
                HttpStatus.OK);
    }

    @GetMapping("/token")
    public String getUsernameFromToken(@RequestParam("token") String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return "Token đã hết hạn!";
        } catch (MalformedJwtException e) {
            return "Token không hợp lệ!";
        } catch (Exception e) {
            return "Lỗi không xác định!";
        }
    }

}
