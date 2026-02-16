package com.order.orderprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for Order Processing Service
 * Integrated with User Authentication Library
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.order.orderprocessing", "com.auth.userauth"})
public class OrderProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessingApplication.class, args);
        System.out.println("Order Processing Service started with User Authentication support");
        System.out.println("Default users: admin (admin123), user (user123)");
    }
}

