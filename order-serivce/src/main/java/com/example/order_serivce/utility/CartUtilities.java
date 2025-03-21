package com.example.order_serivce.utility;

import com.example.order_serivce.domain.Product;

import java.math.BigDecimal;

public class CartUtilities {
    public static BigDecimal getSubTotalForItem(Product product, int quantity){
        return (product.getPrice()).multiply(BigDecimal.valueOf(quantity));
    }
}
