package com.keycraft.service;

import com.keycraft.model.Order.OrderStatus;
import com.keycraft.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public boolean existsInActiveOrders(Long productId) {
        return orderItemRepository.existsByProductIdAndOrderStatusNot(productId, OrderStatus.CANCELLED);
    }
}

