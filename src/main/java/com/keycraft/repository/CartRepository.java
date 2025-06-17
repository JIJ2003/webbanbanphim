package com.keycraft.repository;

import com.keycraft.model.Cart;
import com.keycraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Tìm giỏ hàng theo người dùng.
     *
     * @param user người dùng
     * @return Optional chứa giỏ hàng nếu tồn tại
     */
    Optional<Cart> findByUser(User user);

    /**
     * Kiểm tra xem người dùng đã có giỏ hàng hay chưa.
     *
     * @param user người dùng
     * @return true nếu đã có giỏ
     */
    boolean existsByUser(User user);

    /**
     * Xoá giỏ hàng của người dùng.
     * Thường dùng khi người dùng xoá tài khoản.
     *
     * @param user người dùng
     */
    void deleteByUser(User user);
}
