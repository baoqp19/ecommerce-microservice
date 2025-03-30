package com.example.order_serivce.service.impl;

import com.example.order_serivce.dto.OrderDto;
import com.example.order_serivce.exception.wrapper.OrderNotFoundException;
import com.example.order_serivce.helper.OrderMappingHelper;
import com.example.order_serivce.repository.OrderRepository;
import com.example.order_serivce.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Override
    public List<OrderDto> findAll() {
        log.info("OrderDto List, service; fetch all orders");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderMappingHelper::map)
                .distinct()
                .toList();
    }

    @Override
    public OrderDto findById(Integer orderId) {
        log.info("OrderDto, service; fetch order by id");
        return this.orderRepository.findById(orderId)
                .map(OrderMappingHelper::map)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order with id[%d] not found", orderId)));
    }

    @Override
    public OrderDto save(final OrderDto orderDto) {
        log.info("OrderDto, service; save order");
        return OrderMappingHelper.map(this.orderRepository.save(OrderMappingHelper.map(orderDto)));
    }

    @Override
    public OrderDto update(final OrderDto orderDto) {
        log.info("OrderDto, service; update order");
        return OrderMappingHelper.map(this.orderRepository.save(OrderMappingHelper.map(orderDto)));
    }

    @Override
    public OrderDto update(final Integer orderId, final OrderDto orderDto) {
        log.info("OrderDto, service; update order with orderId");
        return OrderMappingHelper.map(this.orderRepository.save(OrderMappingHelper.map(this.findById(orderId))));
    }

    @Override
    public void deleteById(final Integer orderId) {
        log.info("Void, service; delete order by id");
        this.orderRepository.delete(OrderMappingHelper.map(this.findById(orderId)));
    }

}
