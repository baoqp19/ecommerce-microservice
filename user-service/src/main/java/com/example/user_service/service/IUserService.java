package com.example.user_service.service;

import com.example.user_service.dto.request.SignInForm;
import com.example.user_service.dto.request.SignUpForm;
import com.example.user_service.dto.response.JwtResponse;
import com.example.user_service.entity.User;

import reactor.core.publisher.Mono;

public interface IUserService {
     Mono<User> registerUser(SignUpForm signUpFrom);

     Mono<JwtResponse> login(SignInForm signInForm);
}
