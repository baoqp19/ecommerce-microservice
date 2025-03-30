package com.example.order_serivce.repository;

import com.example.order_serivce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}