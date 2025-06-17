package com.keycraft.service;

import com.keycraft.model.Notification;
import com.keycraft.model.User;
import com.keycraft.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public enum NotificationType {
        CART_ADD, CART_REMOVE, CART_UPDATE, ORDER_PLACED, ORDER_CONFIRMED, ORDER_SHIPPED, ORDER_DELIVERED
    }
    
    public Notification createNotification(User user, String title, String message, Notification.NotificationType type) {
        Notification notification = new Notification(user, title, message, type);
        return notificationRepository.save(notification);
    }
    
    public Notification createCartNotification(User user, String title, String message, NotificationType type) {
        Notification.NotificationType notificationType;
        switch (type) {
            case CART_ADD:
                notificationType = Notification.NotificationType.CART_ADD;
                break;
            case CART_REMOVE:
                notificationType = Notification.NotificationType.CART_REMOVE;
                break;
            case CART_UPDATE:
                notificationType = Notification.NotificationType.CART_UPDATE;
                break;
            case ORDER_PLACED:
                notificationType = Notification.NotificationType.ORDER_PLACED;
                break;
            case ORDER_CONFIRMED:
                notificationType = Notification.NotificationType.ORDER_CONFIRMED;
                break;
            case ORDER_SHIPPED:
                notificationType = Notification.NotificationType.ORDER_SHIPPED;
                break;
            case ORDER_DELIVERED:
                notificationType = Notification.NotificationType.ORDER_DELIVERED;
                break;
            default:
                notificationType = Notification.NotificationType.CART_UPDATE;
        }
        
        Notification notification = new Notification(user, title, message, notificationType);
        return notificationRepository.save(notification);
    }
    
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
    
    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserIdAndReadFalse(user.getId());
    }
    
    public Long getUnreadNotificationCount(User user) {
        return notificationRepository.countUnreadByUserId(user.getId());
    }
    
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
    
    public void markAllAsRead(User user) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(user.getId());
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }
    
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}