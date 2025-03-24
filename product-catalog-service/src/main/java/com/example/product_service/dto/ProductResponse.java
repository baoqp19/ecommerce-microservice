package com.example.product_service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private BigDecimal price;
    private String description;
    private String category;
    private int availability;
}
