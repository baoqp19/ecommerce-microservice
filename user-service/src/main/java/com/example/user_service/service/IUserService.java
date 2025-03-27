package com.example.user_service.service;

import com.example.user_service.dto.request.SignUpForm;
import com.example.user_service.entity.User;

import reactor.core.publisher.Mono;

public interface IUserService {
     Mono<User> registerUser(SignUpForm signUpFrom);
}
