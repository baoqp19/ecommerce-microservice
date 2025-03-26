package com.example.order_serivce.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.order_serivce.dto.request.OrderRequest;
import com.example.order_serivce.service.OrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

    @GetMapping
    public String getString() {
        return "Xin chaof banj nhas";
    }

}
