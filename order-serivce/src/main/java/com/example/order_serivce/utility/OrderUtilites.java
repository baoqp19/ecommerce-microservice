package com.example.order_serivce.utility;

import com.example.order_serivce.domain.Item;

import java.math.BigDecimal;
import java.util.List;

public class OrderUtilites {
    public static BigDecimal countTotalPrice(List<Item> cart){
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < cart.size(); i++){
            total = total.add(cart.get(i).getSubtotal());
        }
        return total;
    }
}
