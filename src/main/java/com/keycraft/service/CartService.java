package com.keycraft.service;

import com.keycraft.model.Cart;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.repository.CartRepository;
import com.keycraft.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }
    
    public Cart addToCart(User user, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);
        
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            int newQuantity = cart.getQuantity() + quantity;
            
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock");
            }
            
            cart.setQuantity(newQuantity);
            return cartRepository.save(cart);
        } else {
            Cart cart = new Cart(user, product, quantity);
            return cartRepository.save(cart);
        }
    }
    
    public Cart updateCartItem(User user, Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        
        if (cart.getProduct().getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }
    
    public void removeFromCart(User user, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        
        cartRepository.delete(cart);
    }
    
    public void clearCart(User user) {
        cartRepository.deleteByUser(user);
    }
    
    public int getCartItemCount(User user) {
        return getCartItems(user).stream()
            .mapToInt(Cart::getQuantity)
            .sum();
    }
}