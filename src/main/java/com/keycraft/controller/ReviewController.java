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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Dùng @RestController cho gọn, không cần @ResponseBody ở từng phương thức
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired private AuthService authService;

    // --- Create Review ---
    // Giữ nguyên @PostMapping("/create") và @RequestParam như code gốc của bạn
    @PostMapping("reviews/create")
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
            Review createdReview = reviewService.createReview(user.getId(), productId, rating, comment);
            // Trả về đối tượng Review vừa tạo với mã 201 CREATED
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred."));
        }
    }

    // --- Update Review ---
    @PutMapping("reviews/update/{reviewId}")
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
            Review updatedReview = reviewService.updateReview(reviewId, user, rating, comment);
            return ResponseEntity.ok(updatedReview);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // --- Delete Review ---
    @DeleteMapping("/{reviewId}")
    @ResponseBody // Giữ nếu vẫn là @Controller
    public ResponseEntity<Map<String, Object>> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Please login to delete review");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            User user = authService.getCurrentUser(authentication); // Lấy đối tượng User
            reviewService.deleteReview(reviewId, user); // <-- TRUYỀN USER VÀO SERVICE

            response.put("success", true);
            response.put("message", "Review deleted successfully");
            return ResponseEntity.ok(response);

        } catch (AccessDeniedException e) { // Bắt lỗi quyền truy cập
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (ResourceNotFoundException e) { // Bắt lỗi không tìm thấy
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // --- Get Reviews ---
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getProductReviews(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Review> reviews = reviewService.getVerifiedProductReviews(productId);
            ReviewService.ReviewStats stats = reviewService.getReviewStats(productId);

            response.put("reviews", reviews);
            response.put("stats", stats);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching reviews: " + e.getMessage()));
        }
    }
}