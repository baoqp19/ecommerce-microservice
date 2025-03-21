package com.example.order_serivce.redis;

import java.util.Collection;

public interface CartRedisRepository {
    void addItemToCart(String key, Object item);
    Collection<Object> getCart(String key, Class type);
    void deleteItemFromCart(String key, Object item);
    void deleteCart(String key);
}
