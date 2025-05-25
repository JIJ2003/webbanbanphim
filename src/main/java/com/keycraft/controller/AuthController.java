package com.keycraft.controller;

import com.keycraft.model.User;
import com.keycraft.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user != null) {
            // Create a safe user object without password
            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("id", user.getId());
            userResponse.put("email", user.getEmail());
            userResponse.put("firstName", user.getFirstName());
            userResponse.put("lastName", user.getLastName());
            userResponse.put("role", user.getRole().toString());
            userResponse.put("profileImageUrl", user.getProfileImageUrl());
            return ResponseEntity.ok(userResponse);
        }
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Unauthorized");
        return ResponseEntity.status(401).body(errorResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = authService.authenticateUser(email, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                session.setAttribute("currentUser", user);
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "role", user.getRole().toString()
                ));
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> userData, HttpSession session) {
        String email = userData.get("email");
        String password = userData.get("password");
        String firstName = userData.get("firstName");
        String lastName = userData.get("lastName");
        String roleStr = userData.get("role");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            User.UserRole role = User.UserRole.CUSTOMER;
            if ("ADMIN".equalsIgnoreCase(roleStr)) {
                role = User.UserRole.ADMIN;
            }
            
            User user = authService.registerUser(email, password, firstName, lastName, role);
            session.setAttribute("currentUser", user);
            
            response.put("success", true);
            response.put("message", "Account created successfully");
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", user.getRole().toString()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.invalidate();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }
}