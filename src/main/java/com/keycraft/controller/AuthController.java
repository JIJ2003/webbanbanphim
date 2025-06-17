package com.keycraft.controller;

import com.keycraft.model.User;
import com.keycraft.service.AuthService;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam(defaultValue = "CUSTOMER") String role,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        try {
            User.UserRole userRole = "ADMIN".equalsIgnoreCase(role) ? User.UserRole.ADMIN : User.UserRole.CUSTOMER;
            User user = authService.registerUser(email, password, firstName, lastName, userRole);

            session.setAttribute("currentUser", user);
            redirectAttributes.addFlashAttribute("success", "Account created successfully!");

            // Chuyển hướng theo role
            return userRole == User.UserRole.ADMIN ? "redirect:/admin" : "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/signup";
        }
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
         Optional<User> userOpt = authService.authenticateUser(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("currentUser", user);

            if (User.UserRole.ADMIN.equals(user.getRole())) {
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
