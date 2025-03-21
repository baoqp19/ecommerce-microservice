package com.example.order_serivce.feignclient;

import com.example.order_serivce.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/")
public interface UserClient {
    @GetMapping(value = "/users/{id}")
    User getUserById(@PathVariable("id") Long id);
}
