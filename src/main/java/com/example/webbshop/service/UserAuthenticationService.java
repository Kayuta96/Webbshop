package com.example.webbshop.service;

import com.example.webbshop.model.User;
import com.example.webbshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String[] roles = user.getRoles().stream()
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role) // Remove "ROLE_" prefix if present
                .toArray(String[]::new);

        // Map User entity to UserDetails for Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roles) // Pass roles without "ROLE_" prefix
                .build();
    }
}
