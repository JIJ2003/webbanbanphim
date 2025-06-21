package com.keycraft.repository;

import com.keycraft.model.Order.OrderStatus;
import com.keycraft.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT COUNT(oi) > 0 
        FROM OrderItem oi 
        WHERE oi.product.id = :productId AND oi.order.status <> :status
    """)
    boolean existsByProductIdAndOrderStatusNot(Long productId, OrderStatus status);
}