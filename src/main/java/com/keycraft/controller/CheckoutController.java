package com.keycraft.controller;

import com.keycraft.model.Order;
import com.keycraft.model.User;
import com.keycraft.service.OrderService;
import com.keycraft.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public String checkoutPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = authService.getCurrentUser(authentication);
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processCheckout(
            Authentication authentication,
            @RequestBody Map<String, String> formData
    ) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Please login to checkout");
            return ResponseEntity.ok(response);
        }

        // Validate required fields
        String[] requiredFields = {"firstName", "lastName", "email", "address", "city", "state", "zip"};
        for (String field : requiredFields) {
            if (!formData.containsKey(field) || formData.get(field).trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Missing required field: " + field);
                return ResponseEntity.ok(response);
            }
        }

        try {
            User user = authService.getCurrentUser(authentication);

            String fullAddress = formData.get("address") + ", "
                    + formData.get("city") + ", "
                    + formData.get("state") + " "
                    + formData.get("zip");

            String paymentMethod = formData.getOrDefault("paymentMethod", "Cash on Delivery");

            Order order = orderService.createOrderFromCart(user, fullAddress, paymentMethod);

            response.put("success", true);
            response.put("message", "Order placed successfully");
            response.put("orderId", order.getId());
            response.put("redirectUrl", "/orders/" + order.getId());
        } catch (IllegalStateException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error processing checkout: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public String checkoutSuccess(@RequestParam Long orderId, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = authService.getCurrentUser(authentication);
        Order order = orderService.getOrderById(orderId).orElse(null);

        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "checkout-success";
    }
}
