package com.example.webbshop.service;

import com.example.webbshop.model.Order;
import com.example.webbshop.model.OrderStatus;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getAllOrders();
    void updateOrderStatus(Long orderId, OrderStatus status);
}
