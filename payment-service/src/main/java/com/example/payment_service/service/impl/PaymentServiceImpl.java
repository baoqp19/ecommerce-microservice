package com.example.payment_service.service.impl;

import com.example.payment_service.constant.AppConstant;
import com.example.payment_service.dto.OrderDto;
import com.example.payment_service.dto.PaymentDto;
import com.example.payment_service.exception.wrapper.PaymentNotFoundException;
import com.example.payment_service.helper.PaymentMappingHelper;
import com.example.payment_service.repository.PaymentRepository;
import com.example.payment_service.service.PaymentService;

import jakarta.transaction.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public List<PaymentDto> findAll() {
        log.info("PaymentDto List, service; fetch all payments");
        return this.paymentRepository.findAll()
                .stream()
                .map(PaymentMappingHelper::map)
                .map(p -> {
                    p.setOrderDto(restTemplate
                            .getForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_API_URL + "/" + p.getOrderDto().getOrderId(), OrderDto.class));
                    return p;
                })
                .distinct()
                .toList();
    }

    @Override
    public PaymentDto findById(Integer paymentId) {
        log.info("PaymentDto, service; fetch payment by id");
        return this.paymentRepository.findById(paymentId)
                .map(PaymentMappingHelper::map)
                .map(p -> {
                    p.setOrderDto(this.restTemplate.getForObject(AppConstant.DiscoveredDomainsApi
                            .ORDER_SERVICE_API_URL + "/" + p.getOrderDto().getOrderId(), OrderDto.class));
                    return p;
                })
                .orElseThrow(() -> new PaymentNotFoundException(String.format("Payment with id[%d] not found", paymentId)));
    }

    @Override
    public PaymentDto save(PaymentDto paymentDto) {
        log.info("PaymentDto, service; save payment");
        return PaymentMappingHelper.map(this.paymentRepository
                .save(PaymentMappingHelper.map(paymentDto)));
    }

    @Override
    public PaymentDto update(PaymentDto paymentDto) {
        log.info("PaymentDto, service; update payment");
        return PaymentMappingHelper.map(this.paymentRepository
                .save(PaymentMappingHelper.map(paymentDto)));
    }

    @Override
    public void deleteById(Integer paymentId) {
        log.info("Void, service; delete payment by id");
        this.paymentRepository.deleteById(paymentId);
    }

}
