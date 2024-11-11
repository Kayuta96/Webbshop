package com.example.webbshop.service;

import com.example.webbshop.model.Cart;
import com.example.webbshop.model.CartItem;
import com.example.webbshop.model.Product;
import com.example.webbshop.repository.CartRepository;
import com.example.webbshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(Cart::new);
    }

    @Transactional
    public void addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        addProductToCartInternal(cart, productId, quantity);
        cartRepository.save(cart); // Save updated cart
    }

    /**
     * Adds a product to a session-based cart for anonymous users.
     */
    public void addProductToSessionCart(Cart sessionCart, Long productId, int quantity) {
        addProductToCartInternal(sessionCart, productId, quantity);
    }

    /**
     * Internal method to handle adding products to either user or session cart.
     */
    private void addProductToCartInternal(Cart cart, Long productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                // Increase quantity if the item exists
                CartItem cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                // Add new item if it doesn't exist in the cart
                CartItem newItem = new CartItem(product, quantity);
                cart.getItems().add(newItem);
            }
        }
    }

    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart); // Save updated cart
    }

    /**
     * Removes a product from the session-based cart.
     */
    public void removeProductFromSessionCart(Cart sessionCart, Long productId) {
        sessionCart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart); // Save updated cart
    }

    /**
     * Merges the session-based cart with the user's persistent cart upon login.
     */
    @Transactional
    public void mergeSessionCartWithUserCart(Long userId, Cart sessionCart) {
        Cart userCart = getCartByUserId(userId);

        for (CartItem sessionItem : sessionCart.getItems()) {
            Optional<CartItem> existingItem = userCart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(sessionItem.getProduct().getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                // If the item already exists in the user's cart, update the quantity
                CartItem userCartItem = existingItem.get();
                userCartItem.setQuantity(userCartItem.getQuantity() + sessionItem.getQuantity());
            } else {
                // If the item does not exist in the user's cart, add it
                userCart.getItems().add(new CartItem(sessionItem.getProduct(), sessionItem.getQuantity()));
            }
        }
        cartRepository.save(userCart); // Save the merged cart to the database
    }
}
