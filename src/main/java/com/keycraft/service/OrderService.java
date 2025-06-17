package com.keycraft.service;

import com.keycraft.model.*;
import com.keycraft.repository.OrderRepository;
import com.keycraft.repository.OrderItemRepository;
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
    
//    @Autowired
//    private NotificationService notificationService;
//    
    @Autowired
    private CartService cartService;
    
    public Order createOrderFromCart(User user) {
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
        
        // Send notification
//        notificationService.createCartNotification(user, 
//            "Order Placed", 
//            "Your order #" + order.getId() + " has been placed successfully",
//            NotificationService.NotificationType.ORDER_PLACED);
        
        return orderRepository.save(order);
    }
    
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
    
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            Order savedOrder = orderRepository.save(order);
            
//            // Send notification based on status
////            String title = "";
////            String message = "";
////            NotificationService.NotificationType notificationType = NotificationService.NotificationType.ORDER_CONFIRMED;
////            
//            switch (status) {
//                case CONFIRMED:
//                    title = "Order Confirmed";
//                    message = "Your order #" + orderId + " has been confirmed";
//                    notificationType = NotificationService.NotificationType.ORDER_CONFIRMED;
//                    break;
//                case SHIPPED:
//                    title = "Order Shipped";
//                    message = "Your order #" + orderId + " has been shipped";
//                    notificationType = NotificationService.NotificationType.ORDER_SHIPPED;
//                    break;
//                case DELIVERED:
//                    title = "Order Delivered";
//                    message = "Your order #" + orderId + " has been delivered";
//                    notificationType = NotificationService.NotificationType.ORDER_DELIVERED;
//                    break;
//                case CANCELLED:
//                    title = "Order Cancelled";
//                    message = "Your order #" + orderId + " has been cancelled";
//                    break;
//            }
//            
//            if (!title.isEmpty()) {
//                notificationService.createCartNotification(order.getUser(), title, message, notificationType);
//            }
            
            return savedOrder;
        }
        return null;
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}