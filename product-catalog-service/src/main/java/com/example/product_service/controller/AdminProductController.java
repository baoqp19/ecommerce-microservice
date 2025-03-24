
package com.example.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.entity.Product;
import com.example.product_service.http.HeaderGenerator;
import com.example.product_service.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private HeaderGenerator headerGenerator;

    @PostMapping(value = "/products")
    private ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest, HttpServletRequest request) {
        if (productRequest != null) {
            try {
                Product product = Product.builder()
                        .productName(productRequest.getProductName())
                        .price(productRequest.getPrice())
                        .description(productRequest.getDescription())
                        .category(productRequest.getCategory())
                        .availability(productRequest.getAvailability())
                        .build();
                productService.addProduct(product);
                return new ResponseEntity<>(
                        product,
                        headerGenerator.getHeadersForSuccessPostMethod(request, product.getId()),
                        HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("Product is not add list {}", productRequest.getProductName());
                return new ResponseEntity<>(
                        headerGenerator.getHeadersForError(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Product>(
                headerGenerator.getHeadersForError(),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/products/{id}")
    private ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            try {
                productService.deleteProduct(id);
                return new ResponseEntity<>(
                        headerGenerator.getHeadersForSuccessGetMethod(),
                        HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                        headerGenerator.getHeadersForError(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }
}
