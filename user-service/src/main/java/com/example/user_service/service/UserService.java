package com.example.user_service.service;

import com.example.user_service.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    User getUserById(Long id);
    User getUserByName(String userName);
    User saveUser(User user);
}
