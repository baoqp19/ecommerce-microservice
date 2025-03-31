package com.example.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.request.SignInForm;
import com.example.user_service.dto.request.SignUpForm;
import com.example.user_service.dto.request.TokenValidationRequest;
import com.example.user_service.dto.request.TokenValidationResponse;
import com.example.user_service.dto.response.JwtResponse;
import com.example.user_service.dto.response.ResponseMessage;
import com.example.user_service.service.TokenValidationService;
import com.example.user_service.service.impl.UserServiceImpl;
import com.example.user_service.service.validate.TokenValidate;


import jakarta.validation.Valid;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final TokenValidationService tokenValidationService;
    private final TokenValidate tokenValidate;

    public AuthController(
            UserServiceImpl userService,
            TokenValidationService tokenValidationService,
            TokenValidate tokenValidate) {
        this.userService = userService;
        this.tokenValidationService = tokenValidationService;
        this.tokenValidate = tokenValidate;
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<ResponseMessage>> register(@Valid @RequestBody SignUpForm signUpForm) {
        return userService.registerUser(signUpForm)
                .flatMap(user -> Mono.just(new ResponseEntity<>(
                        new ResponseMessage("User: " + signUpForm.getUsername() + " create successfully."),
                        HttpStatus.OK)))
                .onErrorResume(error -> Mono
                        .just(new ResponseEntity<>(new ResponseMessage(error.getMessage()), HttpStatus.BAD_REQUEST)));
    }

    @PostMapping("/signin")
    public Mono<ResponseEntity<JwtResponse>> login(@Valid @RequestBody SignInForm signInForm) {
        return userService.login(signInForm)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    JwtResponse errorResponse = new JwtResponse(null, null, null, null);
                    return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED));
                });
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody TokenValidationRequest validationRequest) {
        String accessToken = validationRequest.getAccessToken();

        boolean isValid = tokenValidationService.validateToken(accessToken);

        if (isValid) {
            // Token hợp lệ, có thể thực hiện các xử lý hoặc trả về thông báo thành công
            return ResponseEntity.ok(new TokenValidationResponse("Valid token"));
        } else {
            // Token không hợp lệ hoặc hết hạn
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse("Invalid token"));
        }
    }

}
