package com.example.order_serivce.service;

import com.example.order_serivce.domain.Item;

import java.util.List;

public interface CartService {
    // add item product to cart
    void addItemToCart(String cartId, Long productId, Integer quantity);
    // get cart by id
    List<Object> getCart(String cartId);
    // change item set quantity product
    void changeItemQuantitty(String cartId, Long productId, Integer quantity);
    void deleteItemForCart(String cartId, Long productId);
    public boolean checkIfItemIsExist(String cartId, Long productId);
    public List<Item> getAllItemsFromCart(String cartId);
    public void deleteCart(String cartId);
}
