package com.example.shipping_service.repository;

import com.example.shipping_service.domain.OrderItem;
import com.example.shipping_service.domain.id.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

}