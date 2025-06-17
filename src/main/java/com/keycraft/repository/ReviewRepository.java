package com.keycraft.repository;

import com.keycraft.model.Review;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByProduct(Product product);
    
    List<Review> findByProductId(Long productId);
    
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    
    List<Review> findByUser(User user);
    
    List<Review> findByUserId(Long userId);
    
    Optional<Review> findByUserAndProduct(User user, Product product);
    
    List<Review> findByVerifiedTrue();
    
    List<Review> findByProductIdAndVerifiedTrue(Long productId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.verified = true")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.verified = true")
    Long countVerifiedReviewsByProductId(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.rating = :rating AND r.verified = true")
    Long countByProductIdAndRatingAndVerifiedTrue(@Param("productId") Long productId, @Param("rating") Integer rating);
}