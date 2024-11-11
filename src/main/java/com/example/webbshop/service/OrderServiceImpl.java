package com.example.webbshop.service;

import com.example.webbshop.model.Cart;
import com.example.webbshop.model.Order;
import com.example.webbshop.model.OrderStatus;
import com.example.webbshop.model.User;
import com.example.webbshop.repository.OrderRepository;
import com.example.webbshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order(user, cart.getItems(), cartService.calculateTotalPrice(cart), OrderStatus.PLACED);
        cartService.clearCart(userId);
        return orderRepository.save(order);
    }

    @Override
    public Order placeOrderByUsername(String username) {
        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByUsername(String username) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
