package com.example.product_service.service;

import com.example.product_service.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    CategoryDto findById(final Integer categoryId);
    CategoryDto save(final CategoryDto categoryDto);
    CategoryDto update(final CategoryDto categoryDto);
    CategoryDto update(final Integer categoryId, final CategoryDto categoryDto);
    void deleteById(final Integer categoryId);
}
