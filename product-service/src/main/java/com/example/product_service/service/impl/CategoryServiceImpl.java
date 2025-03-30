package com.example.product_service.service.impl;

import com.example.product_service.dto.CategoryDto;
import com.example.product_service.dto.ProductDto;
import com.example.product_service.exception.wrapper.CategoryNotFoundException;
import com.example.product_service.helper.CategoryMappingHelper;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.service.CategoryService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAll() {
        log.info("Fetching all categories");
        return categoryRepository.findAll().stream()
                .map(CategoryMappingHelper::map)
                .distinct()
                .toList();
    }

    @Override
    public CategoryDto findById(Integer categoryId) {
        log.info("Fetching category by id: {}", categoryId);
        return categoryRepository.findById(categoryId)
                .map(CategoryMappingHelper::map)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " not found"));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        log.info("CategoryDto, service; save category");
        return CategoryMappingHelper
                .map(categoryRepository.save(CategoryMappingHelper.map(categoryDto)));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("CategoryDto Service, update category");
        return CategoryMappingHelper
                .map(categoryRepository.save(CategoryMappingHelper.map(categoryDto)));
    }

    @Override
    public CategoryDto update(Integer categoryId, CategoryDto categoryDto) {
        log.info("CategoryDto Service, update category with categoryId");
        return CategoryMappingHelper
                .map(categoryRepository.save(CategoryMappingHelper.map(this.findById(categoryId))));
    }

    @Override
    public void deleteById(Integer categoryId) {
        log.info("Void Service, delete category by id");
        categoryRepository.deleteById(categoryId);
    }
}
