package com.example.webbshop.controller;

import com.example.webbshop.model.Order;
import com.example.webbshop.model.OrderStatus;
import com.example.webbshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public String placeOrder(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            orderService.placeOrderByUsername(username); // Adjusted to use username
        }
        return "redirect:/orders/history";
    }

    @GetMapping("/history")
    public String viewOrderHistory(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            List<Order> orders = orderService.getOrdersByUsername(username);
            model.addAttribute("orders", orders);
        }
        return "order-history"; // Points to order-history.html
    }

    @GetMapping("/admin")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-orders";
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId, @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/orders/admin";
    }
}
