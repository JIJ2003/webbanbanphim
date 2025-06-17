package com.keycraft.service;

import com.keycraft.model.*;
import com.keycraft.repository.OrderRepository;
import com.keycraft.repository.OrderItemRepository;
import com.keycraft.model.User;
import com.keycraft.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartService cartService;
    
//    @Autowired
//private EmailService emailService;
    
    public Order createOrder(User user, String shippingAddress, String billingAddress, String paymentMethod) {
        List<Cart> cartItems = cartService.getCartItems(user);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Validate stock availability
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        }
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setCustomerEmail(user.getEmail());
        order.setCustomerName(user.getFirstName() + " " + user.getLastName());
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        
        // Calculate total amount
        BigDecimal totalAmount = cartItems.stream()
            .map(cart -> cart.getProduct().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalAmount(totalAmount);
        
        // Save order
        order = orderRepository.save(order);
        
        // Create order items and update stock
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            OrderItem orderItem = new OrderItem(
                order, 
                product, 
                cartItem.getQuantity(), 
                product.getPrice()
            );
            orderItemRepository.save(orderItem);
            
            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // Clear cart
        cartService.clearCart(user);
        
        // Send confirmation email
        //emailService.sendOrderConfirmation(order);
        
        return order;
    }
    
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}