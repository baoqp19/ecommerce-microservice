package com.example.product_service.service;

import java.util.List;

import com.example.product_service.entity.Product;

public interface ProductService {
    List<Product> getAllProduct();

    List<Product> getAllProductByCategory(String category);

    Product getProductById(Long id);

    List<Product> getAllProductsByName(String name);

    Product addProduct(Product product);

    void deleteProduct(Long productId);
}
