package com.example.webbshop.service;

import com.example.webbshop.model.Order;
import com.example.webbshop.model.OrderStatus;

import java.util.List;

public interface OrderService {

    // Existing method for placing an order with userId
    Order placeOrder(Long userId);

    // New method for placing an order by username
    Order placeOrderByUsername(String username);

    // Method to get orders by userId (for internal use if needed)
    List<Order> getOrdersByUserId(Long userId);

    // New method to get orders by username
    List<Order> getOrdersByUsername(String username);

    // Existing method to get all orders (for admin view)
    List<Order> getAllOrders();

    // Method to update order status
    void updateOrderStatus(Long orderId, OrderStatus status);
}
