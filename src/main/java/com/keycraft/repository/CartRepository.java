package com.keycraft.repository;

import com.keycraft.model.Cart;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    List<Cart> findByUser(User user);
    
    Optional<Cart> findByUserAndProduct(User user, Product product);
    
    void deleteByUser(User user);
    
    void deleteByUserAndProduct(User user, Product product);
}