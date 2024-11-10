package com.example.webbshop.controller;

import com.example.webbshop.model.Cart;
import com.example.webbshop.model.Product;
import com.example.webbshop.service.CartService;
import com.example.webbshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public String viewCart(Model model, @RequestParam("userId") Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
        return "cart";
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam("userId") Long userId,
                                   @RequestParam("productId") Long productId,
                                   @RequestParam("quantity") int quantity) {
        cartService.addProductToCart(userId, productId, quantity);
        return "redirect:/cart?userId=" + userId;
    }

    @PostMapping("/remove")
    public String removeProductFromCart(@RequestParam("userId") Long userId,
                                        @RequestParam("productId") Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return "redirect:/cart?userId=" + userId;
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("userId") Long userId, Model model) {
        Cart cart = cartService.getCartByUserId(userId);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
        return "checkout";
    }
}
