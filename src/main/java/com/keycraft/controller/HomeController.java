package com.keycraft.controller;

import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Get featured products for the homepage
        List<Product> featuredProducts = productService.getFeaturedProducts();
        
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("currentUser", currentUser);
        
        return "index";
    }
    
    @GetMapping("/products")
    public String products(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        List<Product> products = productService.getAllProducts();
        
        model.addAttribute("products", products);
        model.addAttribute("currentUser", currentUser);
        
        return "products";
    }
    
    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Check if user is admin
        if (currentUser == null || !User.UserRole.ADMIN.equals(currentUser.getRole())) {
            return "redirect:/login?error=access_denied";
        }
        
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("currentUser", currentUser);
        
        return "admin";
    }
    
    @GetMapping("/login")
    public String login(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return "redirect:/";
        }
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return "redirect:/";
        }
        return "signup";
    }
}