package com.keycraft.controller;

import com.keycraft.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser() {
        // For demo purposes, return a mock authenticated user
        // In a real app, this would get the user from session/JWT token
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@keycraft.com");
        user.setFirstName("Admin");
        user.setLastName("User");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("redirect", "/");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }
}