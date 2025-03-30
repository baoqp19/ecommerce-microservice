package com.example.user_service.repository;

import com.example.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String name);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User getUserByUsername(String username);
    List<User> findAll();
}
