package com.example.webbshop.controller;

import com.example.webbshop.model.Cart;
import com.example.webbshop.model.Product;
import com.example.webbshop.service.CartService;
import com.example.webbshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
@SessionAttributes("sessionCart") // Holds a temporary cart for anonymous users
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
<<<<<<< HEAD
    private final UserService userService;
=======
>>>>>>> parent of 8ee58ed (d)

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

<<<<<<< HEAD
    /**
     * Initializes a session-based cart for anonymous users.
     */
    @ModelAttribute("sessionCart")
    public Cart sessionCart() {
        return new Cart(); // Returns a new Cart object for the session-based cart
    }

    /**
     * Retrieves and displays the current user's cart or the session cart.
     */
    @GetMapping
    public String viewCart(Model model, @ModelAttribute("sessionCart") Cart sessionCart) {
        Cart cart;
        if (isUserAuthenticated()) {
            Long userId = getCurrentUserId();
            cart = cartService.getCartByUserId(userId);
        } else {
            cart = sessionCart; // Use sessionCart for anonymous users
        }
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
        return "cart"; // Returns the view named 'cart.html'
    }

    /**
     * Adds a product to the current user's cart or the session cart if not logged in.
     */
    @PostMapping("/add")
    public String addProductToCart(@RequestParam("productId") Long productId,
                                   @RequestParam("quantity") int quantity,
                                   @ModelAttribute("sessionCart") Cart sessionCart,
                                   HttpSession session) {
        if (isUserAuthenticated()) {
            Long userId = getCurrentUserId();
            cartService.addProductToCart(userId, productId, quantity); // Adds to the persistent user cart
        } else {
            // Adds to the session-based cart for anonymous users
            cartService.addProductToSessionCart(sessionCart, productId, quantity);
            session.setAttribute("sessionCart", sessionCart); // Store sessionCart in the session
        }
        return "redirect:/cart"; // Redirects to the cart view
    }

    /**
     * Removes a product from the current user's cart or the session cart if not logged in.
     */
    @PostMapping("/remove")
    public String removeProductFromCart(@RequestParam("productId") Long productId,
                                        @ModelAttribute("sessionCart") Cart sessionCart,
                                        HttpSession session) {
        if (isUserAuthenticated()) {
            Long userId = getCurrentUserId();
            cartService.removeProductFromCart(userId, productId); // Removes from the persistent user cart
        } else {
            // Removes from the session-based cart for anonymous users
            cartService.removeProductFromSessionCart(sessionCart, productId);
            session.setAttribute("sessionCart", sessionCart); // Update session cart
        }
        return "redirect:/cart";
    }

    /**
     * Displays the checkout page for the current user's cart or the session cart.
     */
    @GetMapping("/checkout")
    public String checkout(Model model, @ModelAttribute("sessionCart") Cart sessionCart) {
        Cart cart;
        if (isUserAuthenticated()) {
            Long userId = getCurrentUserId();
            cart = cartService.getCartByUserId(userId);
        } else {
            cart = sessionCart; // Use session cart for anonymous users
        }
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
        return "checkout"; // Returns the view named 'checkout.html'
    }

    /**
     * Helper method to check if the user is authenticated.
     */
    private boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     */
    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserIdByUsername(userDetails.getUsername());
=======
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
>>>>>>> parent of 8ee58ed (d)
    }
}
