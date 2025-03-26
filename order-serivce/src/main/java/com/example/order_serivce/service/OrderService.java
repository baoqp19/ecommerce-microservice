package com.example.order_serivce.service;

import com.example.order_serivce.dto.OrderItemsDto;
import com.example.order_serivce.dto.request.OrderRequest;
import com.example.order_serivce.dto.response.InventoryResponse;
import com.example.order_serivce.model.Order;
import com.example.order_serivce.model.OrderItems;
import com.example.order_serivce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderItems> orderItems = orderRequest
                .getOrderItemsDtoList()
                .stream()
                .map(this::mapToOrderItemsDto)
                .toList();
        order.setOrderItemsList(orderItems);

        List<String> productNameList = order.getOrderItemsList()
                .stream()
                .map(OrderItems::getProductName)
                .toList();

        // Call inventory-service, and place order if product is in
        // stock
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://localhost:8083/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("productName", productNameList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponsesArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            // hết hàng
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderItems mapToOrderItemsDto(OrderItemsDto orderLineItemsDto) {
        OrderItems orderLineItems = new OrderItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setProductName(orderLineItems.getProductName());
        return orderLineItems;
    }

}
