package com.keycraft.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keycraft.model.Cart;
import com.keycraft.model.CartItem;
import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.repository.CartItemRepository;
import com.keycraft.repository.CartRepository;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

//    @Autowired
//    private NotificationService notificationService;

    /** Lấy Cart mặc định của User, tạo mới nếu chưa có */
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    /** Lấy danh sách item trong cart của User */
    public List<CartItem> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findByCart(cart);
    }

    /** Thêm sản phẩm vào giỏ hàng */
    public CartItem addToCart(User user, Product product, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            CartItem savedItem = cartItemRepository.save(cartItem);

////            notificationService.createCartNotification(user,
//                    "Cart Updated",
//                    "Updated " + product.getName() + " quantity to " + cartItem.getQuantity(),
//                    NotificationService.NotificationType.CART_UPDATE);

            return savedItem;
        } else {
            CartItem cartItem = new CartItem(cart, product, quantity);
            CartItem savedItem = cartItemRepository.save(cartItem);

//            notificationService.createCartNotification(user,
//                    "Added to Cart",
//                    "Added " + product.getName() + " to your cart",
//                    NotificationService.NotificationType.CART_ADD);

            return savedItem;
        }
    }

    /** Cập nhật số lượng item trong cart */
    public void updateCartItemQuantity(User user, Long cartItemId, Integer quantity) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        cartItemOpt.ifPresent(cartItem -> {
            if (cartItem.getCart().getUser().getId().equals(user.getId())) {
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);

//                notificationService.createCartNotification(user,
//                        "Cart Updated",
//                        "Updated " + cartItem.getProduct().getName() + " quantity to " + quantity,
//                        NotificationService.NotificationType.CART_UPDATE);
            }
        });
    }

    /** Xóa một item khỏi cart */
    public void removeFromCart(User user, Long cartItemId) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        cartItemOpt.ifPresent(cartItem -> {
            if (cartItem.getCart().getUser().getId().equals(user.getId())) {
                String productName = cartItem.getProduct().getName();
                cartItemRepository.delete(cartItem);

//                notificationService.createCartNotification(user,
//                        "Removed from Cart",
//                        "Removed " + productName + " from your cart",
//                        NotificationService.NotificationType.CART_REMOVE);
            }
        });
    }

    /** Xóa toàn bộ giỏ hàng */
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);

//            notificationService.createCartNotification(user,
//                    "Cart Cleared",
//                    "Your cart has been cleared",
//                    NotificationService.NotificationType.CART_UPDATE);
        }
    }

    /** Tổng số lượng item trong cart */
    public Long getCartItemCount(User user) {
        return getCartItems(user).stream()
                .mapToLong(CartItem::getQuantity)
                .sum();
    }

    /** Tính tổng tiền trong giỏ */
    public BigDecimal getCartTotal(User user) {
        return getCartItems(user).stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Kiểm tra xem sản phẩm đã có trong cart chưa */
    public boolean hasItemInCart(User user, Product product) {
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findByCartAndProduct(cart, product).isPresent();
    }

    /** Tính tổng tiền từ list item (tùy chọn) */
    public double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();
    }

    /** Lấy lại các item trong cart (alias method) */
    public List<CartItem> getItemsForUser(User user) {
        return getCartItems(user);
    }
}
