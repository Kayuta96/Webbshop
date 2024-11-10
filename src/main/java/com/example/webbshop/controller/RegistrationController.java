package com.example.webbshop.controller;

import com.example.webbshop.model.User;
import com.example.webbshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register"; // Points to register.html
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        if (userService.emailExists(email)) {
            model.addAttribute("error", "Email already in use.");
            return "register";
        }

        userService.registerNewUser(username, email, password);
        return "redirect:/login"; // Redirect to login after successful registration
    }
}
