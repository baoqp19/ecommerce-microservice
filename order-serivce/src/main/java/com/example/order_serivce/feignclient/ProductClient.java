package com.example.order_serivce.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalog-service", url = "http://localhost:8085/")
public interface ProductClient {
    @GetMapping(value = "/products/{id}")
    com.example.order_serivce.domain.Product getProductById(@PathVariable(value = "id") Long productId);
}
