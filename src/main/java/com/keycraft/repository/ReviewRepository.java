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

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<Review> findByProductIdAndVerifiedTrue(Long productId);

    Optional<Review> findByUserAndProduct(User user, Product product);
    Optional<Review> findByIdAndUser(Long id, User user);

    @Query("SELECT r.rating FROM Review r WHERE r.product.id = :productId AND r.verified = true")
    List<Integer> getAllVerifiedRatingsByProductId(@Param("productId") Long productId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.verified = true")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.verified = true")
    Long countVerifiedReviewsByProductId(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.rating = :rating AND r.verified = true")
    Long countByProductIdAndRatingAndVerifiedTrue(@Param("productId") Long productId, @Param("rating") Integer rating);

}