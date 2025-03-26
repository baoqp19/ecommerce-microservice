package com.example.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.entity.User;
import com.example.user_service.http.HeaderGenerator;
import com.example.user_service.repository.IUserRepository;

@RestController
@RequestMapping("api/admin")
public class AdminUserController {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private HeaderGenerator headerGenerator;

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
}
