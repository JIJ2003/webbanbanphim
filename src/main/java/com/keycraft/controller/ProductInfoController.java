package com.keycraft.controller;

import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.model.Review;
import com.keycraft.service.ProductService;
import com.keycraft.service.ReviewService;
import com.keycraft.service.AuthService;
import com.keycraft.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductInfoController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/{id}")
    public String productInfo(@PathVariable Long id, Model model, Authentication authentication) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }
        
        // Get product reviews and statistics
        List<Review> reviews = reviewService.getVerifiedProductReviews(id);
        ReviewService.ReviewStats reviewStats = reviewService.getReviewStats(id);
        
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewStats", reviewStats);
        
        // Add user-specific information if authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            User user = authService.getCurrentUser(authentication);
            model.addAttribute("user", user);
            
            // Check if user has purchased this product (for review verification)
            boolean hasPurchased = reviewService.hasUserPurchasedProduct(user.getId(), id);
            model.addAttribute("hasPurchased", hasPurchased);
            
            // Check if user has already reviewed this product
            boolean hasReviewed = reviewService.hasUserReviewedProduct(user, product);
            model.addAttribute("hasReviewed", hasReviewed);
            
            // Check if product is in cart
            boolean inCart = cartService.hasItemInCart(user, product);
            model.addAttribute("inCart", inCart);
            
            // Get cart count for navbar
            Long cartItemCount = cartService.getCartItemCount(user);
            model.addAttribute("cartItemCount", cartItemCount);
        } else {
            model.addAttribute("cartItemCount", 0L);
        }
        
        return "product-info";
    }
}