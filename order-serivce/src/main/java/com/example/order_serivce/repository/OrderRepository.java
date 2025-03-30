package com.example.order_serivce.repository;

import com.example.order_serivce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}