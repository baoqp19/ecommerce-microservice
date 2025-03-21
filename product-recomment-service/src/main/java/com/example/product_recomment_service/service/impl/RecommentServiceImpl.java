package com.example.product_recomment_service.service.impl;

import com.example.product_recomment_service.model.Recomment;
import com.example.product_recomment_service.repository.RecommentRepository;
import com.example.product_recomment_service.service.RecommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommentServiceImpl implements RecommentService {

    @Autowired
    private RecommentRepository recommentRepository;

    @Override
    public Recomment saveRecommendation(Recomment recomment) {
        return recommentRepository.save(recomment);
    }

    @Override
    public List<Recomment> getAllRecommendationByProductName(String productName) {
        return recommentRepository.findAllRatingByProductName(productName);
    }

    @Override
    public void deleteRecommendation(Long id) {
        recommentRepository.deleteById(id);
    }

    @Override
    public Recomment getRecommendationById(Long recommendationId) {
        return recommentRepository.getOne(recommendationId);
    }

}
