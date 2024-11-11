package com.example.webbshop.model;

import com.example.webbshop.model.Cart;
import com.example.webbshop.service.CartService;
import com.example.webbshop.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final CartService cartService;
    private final UserService userService;

    public LoginSuccessListener(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Long userId = userService.findUserIdByUsername(username);

            // Retrieve session cart and merge with the user cart
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpSession session = attributes.getRequest().getSession();
                Cart sessionCart = (Cart) session.getAttribute("sessionCart");
                if (sessionCart != null) {
                    cartService.mergeSessionCartWithUserCart(userId, sessionCart);
                    session.removeAttribute("sessionCart");
                }
            }
        }
    }
}
