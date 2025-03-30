package com.example.order_serivce.helper;

import com.example.order_serivce.entity.Cart;
import com.example.order_serivce.dto.CartDto;
import com.example.order_serivce.dto.UserDto;

public interface CartMappingHelper {
    public static CartDto map(final Cart cart) {
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .userDto(
                        UserDto.builder()
                                .userId(cart.getUserId())
                                .build())
                .build();
    }

    public static Cart map(final CartDto cartDto) {
        return Cart.builder()
                .cartId(cartDto.getCartId())
                .userId(cartDto.getUserId())
                .build();
    }
}
