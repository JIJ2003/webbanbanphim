package com.keycraft.repository;
import com.keycraft.model.User;

import com.keycraft.model.CartItem;
import com.keycraft.model.Cart;
import com.keycraft.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Lấy tất cả CartItem theo cart
    List<CartItem> findByCart(Cart cart);

    // Tìm CartItem theo cart và product (dùng để kiểm tra sản phẩm đã có trong giỏ chưa)
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Xóa 1 sản phẩm khỏi giỏ hàng
    void deleteByCartAndProduct(Cart cart, Product product);

    // Xóa toàn bộ sản phẩm trong giỏ
    void deleteByCart(Cart cart);

    // Đếm số item trong cart
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Long countByCartId(@Param("cartId") Long cartId);

    // Tính tổng giá trị giỏ hàng
    @Query("SELECT SUM(ci.quantity * ci.product.price) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Double getTotalAmountByCartId(@Param("cartId") Long cartId);

    // Lấy item theo id nhưng phải thuộc đúng cart
    Optional<CartItem> findByIdAndCart(Long id, Cart cart);
}
