package com.keycraft.service;

import com.keycraft.model.Review;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.model.Order;
import com.keycraft.repository.ReviewRepository;
import com.keycraft.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }
    
    public List<Review> getVerifiedProductReviews(Long productId) {
        return reviewRepository.findByProductIdAndVerifiedTrue(productId);
    }
    
    public Review createReview(User user, Product product, Integer rating, String comment) {
        // Check if user has purchased this product
        boolean hasPurchased = hasUserPurchasedProduct(user.getId(), product.getId());
        
        Review review = new Review(user, product, rating, comment, hasPurchased);
        return reviewRepository.save(review);
    }
    
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        List<Order> deliveredOrders = orderRepository.findDeliveredOrdersByUserAndProduct(userId, productId);
        return !deliveredOrders.isEmpty();
    }
    
    public boolean hasUserReviewedProduct(User user, Product product) {
        return reviewRepository.findByUserAndProduct(user, product).isPresent();
    }
    
    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository.getAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }
    
    public Long getReviewCount(Long productId) {
        return reviewRepository.countVerifiedReviewsByProductId(productId);
    }
    
    public ReviewStats getReviewStats(Long productId) {
        Long totalReviews = getReviewCount(productId);
        Double averageRating = getAverageRating(productId);
        
        Long[] starCounts = new Long[5];
        for (int i = 1; i <= 5; i++) {
            starCounts[i-1] = reviewRepository.countByProductIdAndRatingAndVerifiedTrue(productId, i);
        }
        
        return new ReviewStats(totalReviews, averageRating, starCounts);
    }
    
    public void updateReview(Long reviewId, Integer rating, String comment) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setRating(rating);
            review.setComment(comment);
            reviewRepository.save(review);
        }
    }
    
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
    
    public static class ReviewStats {
        private Long totalReviews;
        private Double averageRating;
        private Long[] starCounts; // [1-star, 2-star, 3-star, 4-star, 5-star]
        
        public ReviewStats(Long totalReviews, Double averageRating, Long[] starCounts) {
            this.totalReviews = totalReviews;
            this.averageRating = averageRating;
            this.starCounts = starCounts;
        }
        
        // Getters
        public Long getTotalReviews() { return totalReviews; }
        public Double getAverageRating() { return averageRating; }
        public Long[] getStarCounts() { return starCounts; }
        public Long getStarCount(int stars) { 
            return (stars >= 1 && stars <= 5) ? starCounts[stars-1] : 0L; 
        }
    }
}