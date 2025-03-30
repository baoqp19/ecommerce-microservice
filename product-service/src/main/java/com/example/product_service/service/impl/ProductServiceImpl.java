package com.example.product_service.service.impl;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.exception.wrapper.ProductNotFoundException;
import com.example.product_service.helper.ProductMappingHelper;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> findAll() {
        log.info("ProductDto List, service, fetch all products");
        return productRepository.findAll()
                .stream()
                .map(ProductMappingHelper::map)
                .distinct()
                .toList();
    }

    @Override
    public ProductDto findById(Integer productId) {
        log.info("ProductDto, service; fetch product by id");
        return productRepository.findById(productId)
                .map(ProductMappingHelper::map)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id[%d] not found", productId)));
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        log.info("ProductDto, service; save product");
        return ProductMappingHelper.map(productRepository.save(ProductMappingHelper.map(productDto)));
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        log.info("ProductDto, service; update product");
        return ProductMappingHelper.map(productRepository.save(ProductMappingHelper.map(productDto)));
    }


    @Override
    public ProductDto update(Integer productId, ProductDto productDto) {
        log.info("ProductDto, service; update product with productId");
        return ProductMappingHelper.map(productRepository.save(ProductMappingHelper.map(this.findById(productId))));
    }


    @Override
    public void deleteById(Integer productId) {
        log.info("Void, service; delete product by id");
        this.productRepository.delete(ProductMappingHelper.map(this.findById(productId)));
    }
    
}
