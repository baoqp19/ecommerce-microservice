package com.example.order_serivce.service.impl;

import com.example.order_serivce.constrant.AppConstant;
import com.example.order_serivce.dto.CartDto;
import com.example.order_serivce.dto.UserDto;
import com.example.order_serivce.exception.wrapper.CartNotFoundException;
import com.example.order_serivce.helper.CartMappingHelper;
import com.example.order_serivce.repository.CartRepository;
import com.example.order_serivce.service.CartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public List<CartDto> findAll() {
        log.info("CartDto List, service; fetch all carts");
        return this.cartRepository.findAll()
                .stream()
                .map(CartMappingHelper::map)
                .peek(c -> c.setUserDto(this.restTemplate.getForObject(
                        AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_URL + "/" + c.getUserDto().getUserId(),
                        UserDto.class)))
                .distinct()
                .toList();
    }

    @Override
    public CartDto findById(final Integer cartId) {
        log.info("CartDto, service; fetch cart by id");
        return this.cartRepository.findById(cartId)
                .map(CartMappingHelper::map)
                .map(c -> {
                    c.setUserDto(this.restTemplate.getForObject(
                            AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_URL + "/" + c.getUserDto().getUserId(),
                            UserDto.class));
                    return c;
                })
                .orElseThrow(() -> new CartNotFoundException(String
                        .format("Cart with id: %d not found", cartId)));
    }

    @Override
    public CartDto save(final CartDto cartDto) {
        log.info("CartDto, service; save cart");
        return CartMappingHelper.map(this.cartRepository
                .save(CartMappingHelper.map(cartDto)));
    }

    @Override
    public CartDto update(final CartDto cartDto) {
        log.info("CartDto, service; update cart");
        return CartMappingHelper.map(this.cartRepository
                .save(CartMappingHelper.map(cartDto)));
    }

    @Override
    public CartDto update(final Integer cartId, final CartDto cartDto) {
        log.info("CartDto, service; update cart with cartId");
        return CartMappingHelper.map(this.cartRepository
                .save(CartMappingHelper.map(this.findById(cartId))));
    }

    @Override
    public void deleteById(final Integer cartId) {
        log.info("Void, service; delete cart by id");
        this.cartRepository.deleteById(cartId);
    }

}
