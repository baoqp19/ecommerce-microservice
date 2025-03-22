package com.example.user_service.service;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
