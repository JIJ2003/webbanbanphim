package com.keycraft.service;

import com.keycraft.model.*;
import com.keycraft.model.Order.OrderStatus;
import com.keycraft.repository.OrderRepository;
import com.keycraft.repository.CartItemRepository;
import com.keycraft.repository.OrderItemRepository;

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
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired private ProductService productService;



    public Order createOrderFromCart(User user, String fullAddress, String paymentMethod) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // 1) Tính tổng tiền
        BigDecimal totalAmount = cartItems.stream()
                                          .map(CartItem::getSubtotal)
                                          .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2) Tạo Order chính
        Order order = new Order(user, totalAmount);
        order.setBillingAddress(fullAddress);
        order.setShippingAddress(fullAddress);
        order.setPaymentMethod(paymentMethod);
        order = orderRepository.save(order);

        // 3) Với mỗi CartItem:
        //    - Giảm stock
        //    - Tạo OrderItem và thêm vào order.getOrderItems()
        for (CartItem ci : cartItems) {
            Product prod = ci.getProduct();
            int qty = ci.getQuantity();

            // Giảm stock
            int remaining = prod.getStock() - qty;
            if (remaining < 0) {
                throw new IllegalStateException(
                    "Not enough stock for product: " + prod.getName());
            }
            prod.setStock(remaining);
            productService.saveProduct(prod);

            // Tạo OrderItem
            OrderItem oi = new OrderItem(order, prod, qty, prod.getPrice());
            order.getOrderItems().add(oi);
        }

        // 4) Clear cart
        cartService.clearCart(user);

        // 5) Lưu lại order (cascade cả orderItems)
        return orderRepository.save(order);
    }
    
    public boolean existsInActiveOrders(Long productId) {
        return orderItemRepository.existsByProductIdAndOrderStatusNot(productId, OrderStatus.CANCELLED);
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
            return orderRepository.save(order);
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
