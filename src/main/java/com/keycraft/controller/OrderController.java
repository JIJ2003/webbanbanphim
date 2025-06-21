package com.keycraft.controller;

import com.keycraft.model.Order;
import com.keycraft.model.User;
import com.keycraft.repository.UserRepository;
import com.keycraft.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        String email = auth.getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    @GetMapping
    public String orders(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/auth/login"; // Sửa lại đúng path login nếu bạn đang dùng /auth/*
        }

        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        model.addAttribute("currentUser", user);

        return "orders"; // Trang orders.jsp
    }

    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
    	   User user = getCurrentUser();
    	    if (user == null) {
    	        return "redirect:/auth/login";
    	    }

    	    Optional<Order> optionalOrder = orderService.getOrderById(orderId);
    	    if (optionalOrder.isEmpty()) {
    	        return "redirect:/orders";
    	    }

    	    Order order = optionalOrder.get();

    	    if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
    	        return "redirect:/orders";
    	    }

    	    // ✅ Convert LocalDateTime → java.util.Date để dùng JSTL <fmt:formatDate>
    	    java.util.Date createdAtDate = Timestamp.valueOf(order.getCreatedAt());

    	    model.addAttribute("order", order);
    	    model.addAttribute("currentUser", user);
    	    model.addAttribute("createdAtDate", createdAtDate); // ✅ Thêm dòng này

    	    return "order-details";
    }
}
