package com.example.user_service.service.impl;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleName;
import com.example.user_service.repository.IRoleRepository;
import com.example.user_service.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    IRoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
