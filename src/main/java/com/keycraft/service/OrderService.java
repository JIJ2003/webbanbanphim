package com.keycraft.service;

import com.keycraft.model.*;
import com.keycraft.repository.OrderRepository;
import com.keycraft.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartService cartService;

    public Order createOrderFromCart(User user, String fullAddress, String paymentMethod) {
        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalAmount = totalAmount.add(cartItem.getSubtotal());
        }

        // Create order
        Order order = new Order(user, totalAmount);
        order.setBillingAddress(fullAddress);
        order.setShippingAddress(fullAddress); // Có thể sửa lại nếu có billing/shipping riêng biệt
        order.setPaymentMethod(paymentMethod);

        order = orderRepository.save(order);

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                order,
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice()
            );
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        // Clear cart
        cartService.clearCart(user);

        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public Order updateStatusAndTracking(Long orderId, String status, String trackingCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Order.OrderStatus.valueOf(status)); // Chuyển String thành Enum

        if (order.getStatus() == Order.OrderStatus.SHIPPED) {
            order.setTrackingCode(trackingCode);
        } else {
            order.setTrackingCode(null); // Xóa tracking code nếu không ở trạng thái SHIPPED
        }

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}
