package com.example.order_serivce.dto.response;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageResponse {
    @Size(min = 10, max = 500)
    private String message;
}
