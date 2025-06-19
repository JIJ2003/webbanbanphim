package com.keycraft.controller;

import com.keycraft.model.Order;
import com.keycraft.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderRepository orderRepository;

    // ✅ PUT /api/orders/{id} — cập nhật trackingCode và status
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order inputOrder) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();

        if (inputOrder.getStatus() != null) {
            order.setStatus(inputOrder.getStatus());

            switch (inputOrder.getStatus()) {
                case SHIPPED:
                case DELIVERED:
                    if (inputOrder.getTrackingCode() != null && !inputOrder.getTrackingCode().isBlank()) {
                        order.setTrackingCode(inputOrder.getTrackingCode());
                    }
                    break;
                case PENDING:
                case CONFIRMED:
                    order.setTrackingCode(null);
                    break;
                default:
                    // keep current trackingCode for CANCELLED or others
                    break;
            }
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return ResponseEntity.ok().build(); // avoid 500 error
    }

}

