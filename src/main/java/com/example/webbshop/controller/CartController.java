package com.example.webbshop.controller;

import com.example.webbshop.model.Cart;
import com.example.webbshop.service.CartService;
import com.example.webbshop.service.ProductService;
import com.example.webbshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService; // Assuming UserService provides method to find user ID by username

    @Autowired
    public CartController(CartService cartService, ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * Retrieves the current user's cart and displays it.
     */
    @GetMapping
    public String viewCart(Model model) {
        Long userId = getCurrentUserId();
        Cart cart = cartService.getCartByUserId(userId);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart)); // Calculate total price of items in cart
        return "cart"; // Returns the view named 'cart.html'
    }

    /**
     * Adds a product to the current user's cart.
     */
    @PostMapping("/add")
    public String addProductToCart(@RequestParam("productId") Long productId,
                                   @RequestParam("quantity") int quantity) {
        Long userId = getCurrentUserId();
        cartService.addProductToCart(userId, productId, quantity); // Adds product to the cart
        return "redirect:/cart"; // Redirects to the cart view for the current user
    }

    /**
     * Removes a product from the current user's cart.
     */
    @PostMapping("/remove")
    public String removeProductFromCart(@RequestParam("productId") Long productId) {
        Long userId = getCurrentUserId();
        cartService.removeProductFromCart(userId, productId); // Removes the product from the cart
        return "redirect:/cart"; // Redirects back to the cart view after removing item
    }

    /**
     * Displays the checkout page for the current user's cart.
     */
    @GetMapping("/checkout")
    public String checkout(Model model) {
        Long userId = getCurrentUserId();
        Cart cart = cartService.getCartByUserId(userId);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart)); // Show total price at checkout
        return "checkout"; // Returns the view named 'checkout.html'
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     */
    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserIdByUsername(userDetails.getUsername()); // Assume a method to retrieve userId from username
    }
}
