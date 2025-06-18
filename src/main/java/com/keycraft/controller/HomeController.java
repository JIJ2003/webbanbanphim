package com.keycraft.controller;

import com.keycraft.model.CartItem;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.repository.UserRepository;
import com.keycraft.service.CartService;
import com.keycraft.service.CustomUserDetailsService;
import com.keycraft.service.ProductService;
import jakarta.servlet.http.HttpSession;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("currentUser", user);
        model.addAttribute("featuredProducts", productService.getFeaturedProducts());
        
     // ← **Thêm dòng này** để badge Cart biết có bao nhiêu item
        Long cartItemCount = cartService.getCartItemCount(user);
        model.addAttribute("cartItemCount", cartItemCount);

        return "index";
    }


    
    @GetMapping("/products")
    public String products(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("cartItemCount", cartService.getCartItemCount(currentUser));
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "products";
    }

    
    @GetMapping("/admin")
    public String admin(Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login?error=access_denied";
        }

        // Lấy email từ principal
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !User.UserRole.ADMIN.equals(user.getRole())) {
            return "redirect:/login?error=access_denied";
        }

        model.addAttribute("currentUser", user);
        model.addAttribute("products", productService.getAllProducts());

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