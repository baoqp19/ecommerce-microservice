package com.example.order_serivce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig {
    // public WebClient webClient() {
    // return WebClient.builder().build();
    // }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}