package com.example.product_recomment_service.controller;

import com.example.product_recomment_service.feignclient.ProductClient;
import com.example.product_recomment_service.feignclient.UserClient;
import com.example.product_recomment_service.http.HeaderGenerator;
import com.example.product_recomment_service.model.Product;
import com.example.product_recomment_service.model.Recomment;
import com.example.product_recomment_service.model.User;
import com.example.product_recomment_service.service.RecommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomment")
public class RecommentController {

    @Autowired
    private RecommentService recommentService;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private HeaderGenerator headerGenerator;

    @GetMapping(value = "/recomments")
    private ResponseEntity<List<Recomment>> getAllRating(@RequestParam("name") String productName) {
        List<Recomment> recommendations = recommentService.getAllRecommendationByProductName(productName);
        if (!recommendations.isEmpty()) {
            return new ResponseEntity<List<Recomment>>(
                    recommendations,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<List<Recomment>>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{userId}/recomments/{productId}")
    private ResponseEntity<Recomment> saveRecommendations(
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId,
            @RequestParam("rating") int rating,
            HttpServletRequest request) {

        Product product = productClient.getProductById(productId);
        User user = userClient.getUserById(userId);

        if (product != null && user != null) {
            try {
                Recomment recommendation = new Recomment();
                recommendation.setProduct(product);
                recommendation.setUser(user);
                recommendation.setRating(rating);
                recommentService.saveRecommendation(recommendation);
                return new ResponseEntity<Recomment>(
                        recommendation,
                        headerGenerator.getHeadersForSuccessPostMethod(request, recommendation.getId()),
                        HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Recomment>(
                        headerGenerator.getHeadersForError(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Recomment>(
                headerGenerator.getHeadersForError(),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/recomments/{id}")
    private ResponseEntity<Void> deleteRecommendations(@PathVariable("id") Long id) {
        Recomment recommendation = recommentService.getRecommendationById(id);
        if (recommendation != null) {
            try {
                recommentService.deleteRecommendation(id);
                return new ResponseEntity<Void>(
                        headerGenerator.getHeadersForSuccessGetMethod(),
                        HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Void>(
                        headerGenerator.getHeadersForError(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Void>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }


}
