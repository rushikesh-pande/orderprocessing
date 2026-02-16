package com.order.orderprocessing.controller;

import com.auth.userauth.model.AuthRequest;
import com.auth.userauth.model.AuthResponse;
import com.auth.userauth.model.User;
import com.auth.userauth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Authentication controller for Order Processing Service
 * Provides endpoints for user authentication and authorization
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Register new user endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authenticationService.registerUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    "USER" // Default role
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Get current user information
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = authenticationService.getUserByUsername(username);
        
        if (user.isPresent()) {
            User u = user.get();
            // Don't send password
            u.setPassword(null);
            return ResponseEntity.ok(u);
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found");
    }

    /**
     * Validate token endpoint
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix
            String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
            
            Optional<User> user = authenticationService.validateTokenAndGetUser(jwt);
            
            if (user.isPresent()) {
                return ResponseEntity.ok("Token is valid for user: " + user.get().getUsername());
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token validation failed: " + e.getMessage());
        }
    }

    /**
     * Request DTO for registration
     */
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

