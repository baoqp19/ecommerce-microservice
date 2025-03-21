package com.example.order_serivce.service;

import com.example.order_serivce.domain.Order;

public interface OrderService {
    // save information order product
    Order saveOrder(Order order);
}
