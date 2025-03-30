package com.example.order_serivce.controller;


import com.example.order_serivce.dto.CartDto;
import com.example.order_serivce.dto.response.collection.DtoCollectionResponse;
import com.example.order_serivce.service.CartService;

import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<DtoCollectionResponse<CartDto>> findAll() {
        log.info("CartDto List, controller; fetch all categories");
        return ResponseEntity.ok(new DtoCollectionResponse<>(this.cartService.findAll()));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> findById(
            @PathVariable("cartId") @NotBlank(message = "Input must not be blank") @Valid final String cartId) {
        log.info("CartDto, resource; fetch cart by id");
        return ResponseEntity.ok(this.cartService.findById(Integer.parseInt(cartId)));
    }

    @PostMapping
    public ResponseEntity<CartDto> save(
            @RequestBody @NotNull(message = "Input must not be NULL!") @Valid final CartDto cartDto) {
        log.info("CartDto, resource; save cart");
        return ResponseEntity.ok(this.cartService.save(cartDto));
    }

    @PutMapping
    public ResponseEntity<CartDto> update(
            @RequestBody @NotNull(message = "Input must not be NULL") @Valid final CartDto cartDto) {
        log.info("CartDto, resource; update cart");
        return ResponseEntity.ok(this.cartService.update(cartDto));
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartDto> update(
            @PathVariable("cartId") @NotBlank(message = "Input must not be blank") @Valid final String cartId,
            @RequestBody @NotNull(message = "Input must not be NULL") @Valid final CartDto cartDto) {
        log.info("CartDto, resource; update cart with cartId");
        return ResponseEntity.ok(this.cartService.update(Integer.parseInt(cartId), cartDto));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("cartId") final String cartId) {
        log.info("Boolean, resource; delete cart by id");
        this.cartService.deleteById(Integer.parseInt(cartId));
        return ResponseEntity.ok(true);
    }

}
