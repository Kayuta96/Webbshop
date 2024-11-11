package com.example.webbshop.service;

import com.example.webbshop.model.User;
import com.example.webbshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        // Construct UserDetails with roles directly (use .roles() or .authorities() based on role format)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // Treat email as the username
                .password(user.getPassword()) // Ensure password is already encoded
                .roles(user.getRoles().toArray(new String[0])) // Use .roles() if roles are plain, or .authorities() if prefixed with "ROLE_"
                .build();
    }
}
