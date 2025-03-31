package com.example.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.product_service.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p where (:category IS NULL or p.category =:category)")
    List<Product> findAllByCategory(@Param("cateru") String category);


    List<Product> findAllByProductName(String name);
}