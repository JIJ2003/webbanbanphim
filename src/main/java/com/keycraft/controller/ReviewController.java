package com.keycraft.controller;

import com.keycraft.exception.AccessDeniedException;
import com.keycraft.exception.ResourceNotFoundException;
import com.keycraft.model.Review;
import com.keycraft.model.User;
import com.keycraft.service.AuthService;
import com.keycraft.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired private ReviewService reviewService;
    @Autowired private AuthService authService;

    // --- Create Review ---
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createReview(
            @RequestParam Long productId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please login to write a review."));
        }

        try {
            User user = authService.getCurrentUser(authentication);
            // THAY ĐỔI: Service sẽ tự kiểm tra tất cả các điều kiện ràng buộc
            Review createdReview = reviewService.createReview(user.getId(), productId, rating, comment);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            // Lỗi 404: Không tìm thấy User hoặc Product
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (AccessDeniedException e) {
            // Lỗi 403: Cấm - do người dùng chưa mua hàng
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            // Lỗi 409: Xung đột - do người dùng đã review rồi
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Lỗi 500: Lỗi server không xác định
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred."));
        }
    }

    // --- Update Review ---
    @PostMapping("/update/{reviewId}")
    @ResponseBody
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please login to update your review."));
        }

        try {
            User user = authService.getCurrentUser(authentication);
            // THAY ĐỔI: Truyền vào User để Service kiểm tra quyền sở hữu
            Review updatedReview = reviewService.updateReview(reviewId, user, rating, comment);
            return ResponseEntity.ok(updatedReview);

        } catch (AccessDeniedException e) {
            // Lỗi 403: Cấm - do không phải chủ sở hữu của review
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // --- Delete Review ---
    @DeleteMapping("/{reviewId}")
    @ResponseBody
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please login to delete your review."));
        }

        try {
            User user = authService.getCurrentUser(authentication);
            // THAY ĐỔI: Truyền vào User để Service kiểm tra quyền sở hữu
            reviewService.deleteReview(reviewId, user);
            return ResponseEntity.ok(Map.of("message", "Review deleted successfully."));

        } catch (AccessDeniedException e) {
            // Lỗi 403: Cấm - do không phải chủ sở hữu của review
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

    // --- Get Reviews --- (Phương thức này vẫn giữ nguyên, không cần thay đổi)
    @GetMapping("/product/{productId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductReviews(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Chỉ lấy các review đã được xác thực để hiển thị công khai
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
}