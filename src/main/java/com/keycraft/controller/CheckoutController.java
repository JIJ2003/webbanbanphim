package com.keycraft.controller;

import com.keycraft.model.Cart;
import com.keycraft.model.Order;
import com.keycraft.model.User;
import com.keycraft.repository.UserRepository;
import com.keycraft.service.CartService;
import com.keycraft.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    @Autowired
    private CartService cartService;
    
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
    public String checkout(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Cart> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            return "redirect:/cart?empty=true";
        }
        
        BigDecimal total = cartItems.stream()
            .map(cart -> cart.getProduct().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        
        return "checkout";
    }
    
    @PostMapping("/process")
    public String processCheckout(
            @RequestParam String shippingAddress,
            @RequestParam String billingAddress,
            @RequestParam String paymentMethod,
            @RequestParam String cardNumber,
            @RequestParam String expiryDate,
            @RequestParam String cvv,
            @RequestParam String cardholderName,
            RedirectAttributes redirectAttributes) {
        
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            // Process payment (in a real application, you would integrate with a payment processor)
            String paymentDetails = paymentMethod + " ending in " + cardNumber.substring(cardNumber.length() - 4);
            
            Order order = orderService.createOrder(user, shippingAddress, billingAddress, paymentDetails);
            
            redirectAttributes.addFlashAttribute("success", "Order placed successfully! Order ID: #" + order.getId());
            return "redirect:/orders/" + order.getId();
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
}