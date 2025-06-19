package com.keycraft.repository;

import com.keycraft.model.Order;
import com.keycraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    List<Order> findByUserId(Long userId);
    
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.product.id = :productId AND o.user.id = :userId AND o.status = 'DELIVERED'")
    List<Order> findDeliveredOrdersByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}