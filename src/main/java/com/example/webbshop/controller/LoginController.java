package com.example.webbshop.controller;

import com.example.webbshop.model.User;
import com.example.webbshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    private final AuthenticationService authenticationService;

    @Autowired
    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Renders login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> authenticatedUser = authenticationService.authenticate(email, password);

        if (authenticatedUser.isPresent()) {
            session.setAttribute("user", authenticatedUser.get());
            return "redirect:/products"; // Redirect to products page after login
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}
