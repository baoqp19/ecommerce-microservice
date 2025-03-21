package com.example.order_serivce.service.impl;

import com.example.order_serivce.repository.OrderRepository;
import com.example.order_serivce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.order_serivce.domain.Order;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

}
