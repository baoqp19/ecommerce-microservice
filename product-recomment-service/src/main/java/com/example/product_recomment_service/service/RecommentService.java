package com.example.product_recomment_service.service;

import com.example.product_recomment_service.model.Recomment;

import java.util.List;

public interface RecommentService {
    Recomment getRecommendationById(Long recommendationId);
    Recomment saveRecommendation(Recomment recommendation);
    List<Recomment> getAllRecommendationByProductName(String productName);
    void deleteRecommendation(Long id);
}
