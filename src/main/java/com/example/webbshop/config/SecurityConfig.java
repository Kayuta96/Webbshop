package com.example.webbshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Define URL access permissions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/products", "/register", "/login").permitAll() // Accessible to all users
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin-only access
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // User or admin access
                        .anyRequest().authenticated() // All other requests require authentication
                )
                // Configure form login
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/products", true) // Force redirect to products after login
                        .permitAll()
                )
                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Redirect to home page after logout
                        .permitAll()
                )
                // Disable CSRF for testing purposes (if required for troubleshooting)
                .csrf().disable();

        return http.build();
    }

    // Password encoder for secure password storage
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
