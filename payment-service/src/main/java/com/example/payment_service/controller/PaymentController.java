package com.example.payment_service.controller;

import com.example.payment_service.dto.PaymentDto;
import com.example.payment_service.dto.response.collection.DtoCollectionResponse;
import com.example.payment_service.service.PaymentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<DtoCollectionResponse<PaymentDto>> findAll() {
        log.info("PaymentDto List, controller; fetch all payments");
        return ResponseEntity.ok(new DtoCollectionResponse<>(this.paymentService.findAll()));
    } 
    
     @GetMapping("/inventory-test")
     @ResponseBody
     public Mono<ResponseEntity<List<String>>> callServiceB() {
         String serviceBUrl = "http://localhost:8083/api/inventory?productName=iphone_13,iphone_13_red";
 
         return paymentService.callServiceB(serviceBUrl)
                 .map(ResponseEntity::ok)
                 .defaultIfEmpty(ResponseEntity.notFound().build());
     }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> findById(@PathVariable("paymentId")
                                               @NotBlank(message = "Input must not be blank")
                                               @Valid final String paymentId) {
        log.info("*** PaymentDto, resource; fetch payment by id *");
        return ResponseEntity.ok(this.paymentService.findById(Integer.parseInt(paymentId)));
    } 

    @PostMapping
    public ResponseEntity<PaymentDto> save(@RequestBody
                                           @NotNull(message = "Input must not be NULL")
                                           @Valid final PaymentDto paymentDto) {
        log.info("PaymentDto, resource; save payment");
        return ResponseEntity.ok(this.paymentService.save(paymentDto));
    }

    @PutMapping
    public ResponseEntity<PaymentDto> update(@RequestBody
                                             @NotNull(message = "Input must not be NULL")
                                             @Valid final PaymentDto paymentDto) {
        log.info("PaymentDto, resource; update payment");
        return ResponseEntity.ok(this.paymentService.update(paymentDto));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("paymentId") final String paymentId) {
        log.info("Boolean, resource; delete payment by id");
        this.paymentService.deleteById(Integer.parseInt(paymentId));
        return ResponseEntity.ok(true);
    }

}
