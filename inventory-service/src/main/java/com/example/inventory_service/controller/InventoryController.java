package com.example.inventory_service.controller;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.exception.UnauthorizedException;
import com.example.inventory_service.service.InventoryService;
import com.example.inventory_service.service.TokenValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    @Autowired
    private final InventoryService inventoryService;

    @Autowired
    private final TokenValidationService tokenValidationService;

    @Autowired
    private final WebClient.Builder webClientBuilder;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    // http://localhost:8083/api/inventory/iphone_13,iphone13_red

    // http://localhost:8083/api/inventory?productName=iphone_13&productName=iphone13_red
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStockNoAccessToken(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestParam List<String> productName) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        if (JwtValidate.validateToken(jwtToken)) {
            log.info("Received inventory check request for skuCode: {}", productName);
            return inventoryService.isInStock(productName);
        }
        return List.of(new InventoryResponse(null, false));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getTokenUserService")
    public String getOrderDetails(@RequestHeader(name = "Authorization") String authorizationHeader) {
        // Kiểm tra tính hợp lệ của token trước khi gọi API
        if (JwtValidate.validateToken(authorizationHeader)) {
            return inventoryService.getTokenUserService(authorizationHeader);
        } else {
            return "Unauthorized accessToken";
        }
    }
}