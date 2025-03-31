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
    private String userServiceBaseUrl; // URL của user-service

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red

    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
    // @GetMapping
    // @ResponseStatus(HttpStatus.OK)
    // public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode)
    // {
    // log.info("Received inventory check request for skuCode: {}", skuCode);
    // return inventoryService.isInStock(skuCode);
    // }

    @GetMapping("/get-order-details")
    public String getOrderDetails(@RequestHeader(name = "Authorization") String authorizationHeader) {
        // Sử dụng JWT từ tiêu đề "Authorization" của yêu cầu gọi API
        String jwtToken = authorizationHeader.replace("Bearer ", "");

        // Sử dụng JWT trong tiêu đề của yêu cầu gọi API
        String apiEndpoint = userServiceBaseUrl + "/api/manager"; // URL của API trong user-service
        String response = webClientBuilder.baseUrl(apiEndpoint)
                .build()
                .get()
                .uri("/token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<InventoryResponse>> isInStockAccessToken(@RequestParam List<String> productName,
            @RequestHeader("Authorization") String authorizationHeader) {

        log.info("Received inventory check request for skuCode: {}", productName);

        // Trích xuất token từ header "Authorization"
        String token = extractTokenFromAuthorizationHeader(authorizationHeader);

        // Gửi access token đến user-service để xác thực
        return inventoryService.requestTokenValidation(token)
                .flatMap(validationMessage -> {
                    if ("Valid token".equals(validationMessage)) {
                        // Access token hợp lệ, tiếp tục xử lý yêu cầu
                        return Mono.just(inventoryService.isInStock(productName, token));
                    } else {
                        // Access token không hợp lệ, trả về lỗi hoặc xử lý khác tùy ý
                        return Mono.error(new UnauthorizedException("Invalid token"));
                    }
                });
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<InventoryResponse>> isInStock(@RequestParam List<String> productName,
            @RequestHeader("Authorization") String authorizationHeader) {

        log.info("Received inventory check request for skuCode: {}", productName);
        // Trích xuất token từ header "Authorization"
        String token = extractTokenFromAuthorizationHeader(authorizationHeader);

        // Xác thực token
        return tokenValidationService.validateToken(token)
                .flatMap(valid -> {
                    if (valid) {
                        // Token hợp lệ, tiếp tục xử lý yêu cầu
                        return Mono.just(inventoryService.isInStock(productName));
                    } else {
                        // Token không hợp lệ, trả về lỗi hoặc xử lý khác tùy ý
                        return Mono.error(new UnauthorizedException("Invalid token"));
                    }
                });
    }

    // Hàm để trích xuất token từ header "Authorization"
    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Loại bỏ "Bearer " để lấy token
        }
        return null;
    }
}