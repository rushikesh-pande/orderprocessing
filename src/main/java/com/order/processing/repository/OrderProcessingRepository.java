package com.order.processing.repository;

import com.order.processing.entity.OrderProcessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderProcessingRepository extends JpaRepository<OrderProcessing, Long> {
    
    Optional<OrderProcessing> findByOrderId(String orderId);
    
    boolean existsByOrderId(String orderId);
}

