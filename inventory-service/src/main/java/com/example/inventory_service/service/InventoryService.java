package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;

    @Autowired
    private final WebClient.Builder webClientBuilder;

    @org.springframework.beans.factory.annotation.Value("${user-service.base-url}")
    private String userServiceBaseUrl; // URL của user-service

    // get Token in -> user-service
    public Mono<String> getTokenFromUserService() {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/manager/token")
                .retrieve()
                .bodyToMono(String.class);
    }

    
    public String getTokenUserService(String authorizationHeader) {
        // Sử dụng JWT từ tiêu đề "Authorization" của yêu cầu gọi API
        String jwtToken = authorizationHeader.replace("Bearer ", "");

        // Token hợp lệ, tiếp tục gọi API từ user-service
        String responseToken = webClientBuilder.baseUrl(userServiceBaseUrl + "/api/manager")
                .build() // chuyển WebClientBuilder -> WebClient
                .get() // GET
                .uri("/token") // Endpoint
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken) // title
                .retrieve() // call HTTP and return ClientResponse
                .bodyToMono(String.class) // transaction content ClientResponse for Mono.
                .block(); // Chờ cho đến khi Mono hoàn thành và trả về giá trị cuối cùng của nó.

        return responseToken;
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> productName) {
        log.info("Checking Inventory"); // còn hàng hay không
        return inventoryRepository.findByProductNameIn(productName)
                .stream()
                .map(inventory -> InventoryResponse.builder()
                        .productName(inventory.getProductName())
                        .isInStock(inventory.getQuantity() > 0)
                        .build())
                .toList();
    }

}
