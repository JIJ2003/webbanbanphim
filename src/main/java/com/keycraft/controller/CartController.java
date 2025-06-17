package com.keycraft.controller;

import com.keycraft.model.Cart;
import com.keycraft.model.User;
import com.keycraft.repository.UserRepository;
import com.keycraft.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }
        
        String email = auth.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping
    public ResponseEntity<List<Cart>> getCartItems() {
        try {
            User user = getCurrentUser();
            List<Cart> cartItems = cartService.getCartItems(user);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
    
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {
        try {
            User user = getCurrentUser();
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            Cart cart = cartService.addToCart(user, productId, quantity);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product added to cart",
                "cartItem", cart
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Authentication required"
            ));
        }
    }
    
    @PutMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @PathVariable Long cartId, 
            @RequestBody Map<String, Object> request) {
        try {
            User user = getCurrentUser();
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            Cart cart = cartService.updateCartItem(user, cartId, quantity);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cart updated",
                "cartItem", cart
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Authentication required"
            ));
        }
    }
    
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long cartId) {
        try {
            User user = getCurrentUser();
            cartService.removeFromCart(user, cartId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item removed from cart"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Authentication required"
            ));
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCartCount() {
        try {
            User user = getCurrentUser();
            int count = cartService.getCartItemCount(user);
            
            return ResponseEntity.ok(Map.of(
                "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "count", 0
            ));
        }
    }
}