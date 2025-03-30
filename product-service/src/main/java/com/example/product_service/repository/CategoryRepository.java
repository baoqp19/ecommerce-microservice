package com.example.product_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product_service.domain.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
