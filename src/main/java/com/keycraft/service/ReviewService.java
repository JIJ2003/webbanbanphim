package com.keycraft.service;

import com.keycraft.exception.AccessDeniedException;
import com.keycraft.exception.ResourceNotFoundException;
import com.keycraft.model.Review;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.repository.ReviewRepository;
import com.keycraft.repository.OrderRepository;
import com.keycraft.repository.ProductRepository;
import com.keycraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;   // Thêm để lấy đối tượng User
    @Autowired private ProductRepository productRepository; // Thêm để lấy đối tượng Product

    public List<Review> getVerifiedProductReviews(Long productId) {
        return reviewRepository.findByProductIdAndVerifiedTrue(productId);
    }

    // THAY ĐỔI LỚN: Hoàn thiện logic tạo review với đầy đủ ràng buộc
    public Review createReview(Long userId, Long productId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // RÀNG BUỘC 1: KIỂM TRA ĐÃ MUA HÀNG CHƯA
        boolean hasPurchased = orderRepository.existsDeliveredOrderByUserAndProduct(userId, productId);
        if (!hasPurchased) {
            throw new AccessDeniedException("You can only review products you have purchased and received.");
        }

        // RÀNG BUỘC 2: KIỂM TRA ĐÃ REVIEW SẢN PHẨM NÀY CHƯA
        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new IllegalStateException("You have already reviewed this product.");
        }

        Review review = new Review(user, product, rating, comment, true); // `verified` luôn là true vì đã qua bước kiểm tra
        return reviewRepository.save(review);
    }

    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        return orderRepository.existsDeliveredOrderByUserAndProduct(userId, productId);
    }

    public boolean hasUserReviewedProduct(User user, Product product) {
        return reviewRepository.findByUserAndProduct(user, product).isPresent();
    }

    // THAY ĐỔI LỚN: Thêm tham số User để kiểm tra quyền sở hữu
    public Review updateReview(Long reviewId, User user, Integer rating, String comment) {
        // RÀNG BUỘC BẢO MẬT: Đảm bảo người dùng chỉ có thể sửa review của chính mình
        Review review = reviewRepository.findByIdAndUser(reviewId, user)
                .orElseThrow(() -> new AccessDeniedException("You do not have permission to update this review."));

        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    // THAY ĐỔI LỚN: Thêm tham số User để kiểm tra quyền sở hữu
    public void deleteReview(Long reviewId, User user) {
        // RÀNG BUỘC BẢO MẬT: Đảm bảo người dùng chỉ có thể xóa review của chính mình
        if (!reviewRepository.findByIdAndUser(reviewId, user).isPresent()) {
            throw new AccessDeniedException("You do not have permission to delete this review.");
        }
        reviewRepository.deleteById(reviewId);
    }

    // --- Các phương thức thống kê giữ nguyên như cũ ---

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