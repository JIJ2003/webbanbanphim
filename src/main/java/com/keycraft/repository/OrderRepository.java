package com.keycraft.repository;

import com.keycraft.model.Order;
import com.keycraft.model.Order.OrderStatus;
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

<<<<<<< HEAD
    // PHƯƠNG THỨC MỚI: Dùng cho Admin Dashboard để lấy tất cả đơn hàng
    List<Order> findAllByOrderByCreatedAtDesc();

    // PHƯƠNG THỨC ĐƯỢC TỐI ƯU: Dùng cho ReviewService để kiểm tra điều kiện mua hàng
    @Query("SELECT count(o) > 0 FROM Order o JOIN o.orderItems i " +
            "WHERE o.user.id = :userId AND i.product.id = :productId AND o.status = 'DELIVERED'")
    boolean existsDeliveredOrderByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    // Phương thức cũ, bạn có thể giữ lại nếu dùng ở nơi khác hoặc xóa đi
    // @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.product.id = :productId AND o.user.id = :userId AND o.status = 'DELIVERED'")
    // List<Order> findDeliveredOrdersByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
=======
    
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.product.id = :productId AND o.user.id = :userId AND o.status = 'DELIVERED'")
    List<Order> findDeliveredOrdersByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

	boolean existsByUserIdAndStatusNot(Long id, OrderStatus cancelled);

	void deleteAllByUserIdAndStatus(Long id, OrderStatus cancelled);

>>>>>>> NTT
}