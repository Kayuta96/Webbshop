package com.example.webbshop.repository;

import com.example.webbshop.model.Order;
import com.example.webbshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Finds orders by user ID
    List<Order> findByUserId(Long userId);

    // New method to find orders by User entity
    List<Order> findByUser(User user);
}
