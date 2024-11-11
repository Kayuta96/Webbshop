package com.example.webbshop.service;

import com.example.webbshop.model.User;
import com.example.webbshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Checks if an email already exists in the system.
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Finds a user's ID based on their username.
     */
    public Long findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Registers a new user with a specified role.
     */
    public void registerNewUser(String username, String email, String password, String role) {
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // Encode password
        newUser.getRoles().add(role); // Adds specified role without prefix

        userRepository.save(newUser);
    }

    /**
     * Registers a new user with a default "USER" role.
     */
    public void registerNewUser(String username, String email, String password) {
        registerNewUser(username, email, password, "USER");
    }

    /**
     * Retrieves all users from the system.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Assigns a role to an existing user if they don't already have it.
     */
    public void assignRoleToUser(String email, String role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (!user.getRoles().contains(role)) { // Check to avoid duplicate roles
            user.getRoles().add(role); // Add role without prefix
            userRepository.save(user);
        }
    }

    /**
     * Finds a user by their username.
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
