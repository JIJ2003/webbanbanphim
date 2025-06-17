package com.keycraft.controller;

import com.keycraft.model.Review;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.service.ReviewService;
import com.keycraft.service.ProductService;
import com.keycraft.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createReview(
            @RequestParam Long productId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Please login to write a review");
            return ResponseEntity.ok(response);
        }
        
        try {
            User user = authService.getCurrentUser(authentication);
            Product product = productService.findById(productId);
            
            if (product == null) {
                response.put("success", false);
                response.put("message", "Product not found");
                return ResponseEntity.ok(response);
            }
            
            if (reviewService.hasUserReviewedProduct(user, product)) {
                response.put("success", false);
                response.put("message", "You have already reviewed this product");
                return ResponseEntity.ok(response);
            }
            
            Review review = reviewService.createReview(user, product, rating, comment);
            
            response.put("success", true);
            response.put("message", "Review submitted successfully");
            response.put("review", review);
            response.put("verified", review.getVerified());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating review: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/product/{productId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductReviews(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Review> reviews = reviewService.getVerifiedProductReviews(productId);
            ReviewService.ReviewStats stats = reviewService.getReviewStats(productId);
            
            response.put("success", true);
            response.put("reviews", reviews);
            response.put("stats", stats);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching reviews: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/update/{reviewId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Please login to update review");
            return ResponseEntity.ok(response);
        }
        
        try {
            reviewService.updateReview(reviewId, rating, comment);
            
            response.put("success", true);
            response.put("message", "Review updated successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating review: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{reviewId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Please login to delete review");
            return ResponseEntity.ok(response);
        }
        
        try {
            reviewService.deleteReview(reviewId);
            
            response.put("success", true);
            response.put("message", "Review deleted successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting review: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}