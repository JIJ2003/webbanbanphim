package com.keycraft.controller;

import com.keycraft.model.CartItem;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.service.AuthService;
import com.keycraft.service.CartService;
import com.keycraft.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthService authService;

    // ====================== PAGE RENDER ======================

    @GetMapping
    public String cartPage(Model model, Authentication authentication) {
        if (isNotAuthenticated(authentication)) return "redirect:/auth/login";

        User user = authService.getCurrentUser(authentication);
        List<CartItem> cartItems = cartService.getCartItems(user);

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        model.addAttribute("cartItemCount", cartService.getCartItemCount(user));

        return "cart";
    }

    // ====================== API ACTIONS ======================

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam Long productId,
                                       @RequestParam(defaultValue = "1") Integer quantity,
                                       Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (isNotAuthenticated(authentication)) {
            return responseFail(response, "Please login to add items to cart");
        }

        try {
            User user = authService.getCurrentUser(authentication);
            Product product = productService.findById(productId);

            if (product == null) {
                return responseFail(response, "Product not found");
            }

            if (product.getStock() < quantity) {
                return responseFail(response, "Insufficient stock");
            }

            CartItem cartItem = cartService.addToCart(user, product, quantity);

            response.put("success", true);
            response.put("message", "Product added to cart successfully");
            response.put("cartItem", cartItem);
            response.put("cartItemCount", cartService.getCartItemCount(user));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return responseFail(response, "Error adding to cart: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateCartItem(@RequestParam Long cartItemId,
                                            @RequestParam Integer quantity,
                                            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (isNotAuthenticated(authentication)) {
            return responseFail(response, "Please login to update cart");
        }

        try {
            User user = authService.getCurrentUser(authentication);
            cartService.updateCartItemQuantity(user, cartItemId, quantity);

            response.put("success", true);
            response.put("message", "Cart updated successfully");
            response.put("cartTotal", cartService.getCartTotal(user));
            response.put("cartItemCount", cartService.getCartItemCount(user));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return responseFail(response, "Error updating cart: " + e.getMessage());
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@RequestParam Long cartItemId,
                                            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (isNotAuthenticated(authentication)) {
            return responseFail(response, "Please login to remove items from cart");
        }

        try {
            User user = authService.getCurrentUser(authentication);
            cartService.removeFromCart(user, cartItemId);

            response.put("success", true);
            response.put("message", "Item removed from cart");
            response.put("cartTotal", cartService.getCartTotal(user));
            response.put("cartItemCount", cartService.getCartItemCount(user));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return responseFail(response, "Error removing item: " + e.getMessage());
        }
    }

    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<?> clearCart(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (isNotAuthenticated(authentication)) {
            return responseFail(response, "Please login to clear cart");
        }

        try {
            User user = authService.getCurrentUser(authentication);
            cartService.clearCart(user);

            response.put("success", true);
            response.put("message", "Cart cleared successfully");
            response.put("cartItemCount", 0L);
            response.put("cartTotal", BigDecimal.ZERO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return responseFail(response, "Error clearing cart: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (isNotAuthenticated(authentication)) {
            response.put("cartItemCount", 0L);
        } else {
            User user = authService.getCurrentUser(authentication);
            response.put("cartItemCount", cartService.getCartItemCount(user));
        }

        return ResponseEntity.ok(response);
    }

    // ====================== HELPER ======================

    private boolean isNotAuthenticated(Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated();
    }

    private ResponseEntity<Map<String, Object>> responseFail(Map<String, Object> response, String message) {
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}
