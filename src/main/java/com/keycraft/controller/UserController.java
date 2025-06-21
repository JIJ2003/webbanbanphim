package com.keycraft.controller;

import com.keycraft.model.Order.OrderStatus;
import com.keycraft.model.User;
import com.keycraft.repository.CartRepository;
import com.keycraft.repository.OrderRepository;
import com.keycraft.repository.UserRepository;
import com.keycraft.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          UserService userService,
                          OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderRepository = orderRepository; // <-- thêm dòng này
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found")));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User newUser) {
        User currentUser = getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "You need to login"));
        }

        if (currentUser.getRole() != User.UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Admin access required"));
        }

        try {
            User created = userService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Failed to create user", "error", e.getMessage()));
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        User currentUser = getAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "You need to login"));
        }

        if (currentUser.getRole() != User.UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Admin access required"));
        }

        // 🔧 Xử lý password tại đây nếu trống thì giữ nguyên
        Optional<User> existingOpt = userRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        User existing = existingOpt.get();

        // Nếu password trống thì giữ nguyên
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isBlank()) {
            updatedUser.setPassword(existing.getPassword()); // đã được mã hoá
        } else {
            updatedUser.setPassword(userService.encodePassword(updatedUser.getPassword()));
        }

        updatedUser.setId(id); // đảm bảo ID không bị mất

        User saved = userRepository.save(updatedUser);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @Transactional // ✅ THÊM ANNOTATION NÀY
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();

        User user = optionalUser.get();

        boolean hasNonCancelledOrders = orderRepository
            .existsByUserIdAndStatusNot(id, OrderStatus.CANCELLED);

        if (hasNonCancelledOrders) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Cannot delete user with non-cancelled orders");
        }

        cartRepository.deleteAllByUserId(id);

        orderRepository.deleteAllByUserIdAndStatus(id, OrderStatus.CANCELLED);

        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }


<<<<<<< HEAD
}
=======


}
>>>>>>> NTT
