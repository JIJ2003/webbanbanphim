package com.keycraft.controller;

import com.keycraft.model.Order;
import com.keycraft.model.User;
import com.keycraft.repository.UserRepository;
import com.keycraft.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        
        String email = auth.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
    
    @GetMapping
    public String orders(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        model.addAttribute("currentUser", user);
        
        return "orders";
    }
    
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            Object order = orderService.getOrderById(orderId);
            
            // Check if user owns this order
            if (!((Order) order).getUser().getId().equals(user.getId())) {
                return "redirect:/orders";
            }
            
            model.addAttribute("order", order);
            model.addAttribute("currentUser", user);
            
            return "order-details";
        } catch (RuntimeException e) {
            return "redirect:/orders";
        }
    }
}