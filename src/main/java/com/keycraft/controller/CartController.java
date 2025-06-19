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

    @Autowired private CartService cartService;
    @Autowired private ProductService productService;
    @Autowired private AuthService authService;

    // ====================== PAGE RENDER ======================
    @GetMapping
    public String cartPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
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
    public ResponseEntity<Map<String,Object>> addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value="quantity", defaultValue="1") int quantity,
            Authentication authentication) {

        Map<String,Object> resp = new HashMap<>();
        if (authentication == null || !authentication.isAuthenticated()) {
            resp.put("success", false);
            resp.put("message", "Please login to add items to cart");
            return ResponseEntity.ok(resp);
        }

        try {
            User user = authService.getCurrentUser(authentication);
            Product p = productService.findById(productId);
            if (p == null) {
                resp.put("success", false);
                resp.put("message", "Product not found");
                return ResponseEntity.ok(resp);
            }
            if (p.getStock() < quantity) {
                resp.put("success", false);
                resp.put("message", "Insufficient stock");
                return ResponseEntity.ok(resp);
            }
            CartItem ci = cartService.addToCart(user, p, quantity);
            resp.put("success", true);
            resp.put("message", "Added to cart");
            resp.put("cartItemCount", cartService.getCartItemCount(user));
            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.put("success", false);
            resp.put("message", "Error: "+ex.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> updateCartItem(
            @RequestParam Long cartItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        Map<String,Object> resp = new HashMap<>();
        if (authentication == null || !authentication.isAuthenticated()) {
            resp.put("success", false);
            resp.put("message", "Please login to update cart");
            return ResponseEntity.ok(resp);
        }

        try {
            User user = authService.getCurrentUser(authentication);
            cartService.updateCartItemQuantity(user, cartItemId, quantity);
            resp.put("success", true);
            resp.put("message", "Cart updated successfully");
            resp.put("cartItemCount", cartService.getCartItemCount(user));
            resp.put("cartTotal", cartService.getCartTotal(user));
            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.put("success", false);
            resp.put("message", "Error: "+ex.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> removeFromCart(
            @RequestParam Long cartItemId,
            Authentication authentication) {

        Map<String,Object> resp = new HashMap<>();
        if (authentication == null || !authentication.isAuthenticated()) {
            resp.put("success", false);
            resp.put("message", "Please login");
            return ResponseEntity.ok(resp);
        }
        try {
            User user = authService.getCurrentUser(authentication);
            cartService.removeFromCart(user, cartItemId);
            resp.put("success", true);
            resp.put("message", "Removed item");
            resp.put("cartItemCount", cartService.getCartItemCount(user));
            resp.put("cartTotal", cartService.getCartTotal(user));
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.put("success", false);
            resp.put("message", "Error: "+ex.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> clearCart(Authentication authentication) {
        Map<String,Object> resp = new HashMap<>();
        if (authentication == null || !authentication.isAuthenticated()) {
            resp.put("success", false);
            resp.put("message", "Please login");
            return ResponseEntity.ok(resp);
        }
        try {
            User user = authService.getCurrentUser(authentication);
            cartService.clearCart(user);
            resp.put("success", true);
            resp.put("message", "Cart cleared");
            resp.put("cartItemCount", 0);
            resp.put("cartTotal", BigDecimal.ZERO);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.put("success", false);
            resp.put("message", "Error: "+ex.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCartCount(Authentication authentication) {
        Map<String,Object> resp = new HashMap<>();
        if (authentication==null || !authentication.isAuthenticated()) {
            resp.put("cartItemCount", 0);
        } else {
            User u = authService.getCurrentUser(authentication);
            resp.put("cartItemCount", cartService.getCartItemCount(u));
        }
        return ResponseEntity.ok(resp);
    }
}
