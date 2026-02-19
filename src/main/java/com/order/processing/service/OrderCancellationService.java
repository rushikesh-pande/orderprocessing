package com.order.processing.service;

import com.order.processing.dto.CancellationRequest;
import com.order.processing.dto.CancellationResponse;
import com.order.processing.kafka.OrderCancellationEventProducer;
import com.order.processing.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCancellationService {

    private final OrderCancellationEventProducer eventProducer;

    @Transactional
    public CancellationResponse cancelOrder(CancellationRequest request) {
        log.info("Processing cancellation request for order: {}", request.getOrderId());

        // Validate if order can be cancelled
        validateCancellationEligibility(request.getOrderId());

        // Create cancellation response
        CancellationResponse response = new CancellationResponse();
        response.setOrderId(request.getOrderId());
        response.setStatus(OrderStatus.CANCELLED);
        response.setCancelledAt(LocalDateTime.now());
        response.setMessage("Order cancelled successfully");

        // Calculate refund amount (in real system, fetch from order details)
        Double refundAmount = calculateRefundAmount(request.getOrderId());
        response.setRefundAmount(refundAmount);
        response.setRefundStatus("INITIATED");

        // Publish order cancelled event to Kafka
        eventProducer.publishOrderCancelled(request.getOrderId(), request.getCustomerId(), 
            request.getReason(), refundAmount);

        log.info("Order {} cancelled successfully. Refund amount: {}", 
            request.getOrderId(), refundAmount);

        return response;
    }

    public boolean canCancelOrder(String orderId) {
        // Check if order is in a cancellable state
        // In real system, fetch order status from database
        OrderStatus currentStatus = getOrderStatus(orderId);
        
        return currentStatus == OrderStatus.CREATED || 
               currentStatus == OrderStatus.PENDING_PAYMENT ||
               currentStatus == OrderStatus.PAYMENT_CONFIRMED ||
               currentStatus == OrderStatus.PROCESSING;
    }

    private void validateCancellationEligibility(String orderId) {
        if (!canCancelOrder(orderId)) {
            throw new IllegalStateException(
                "Order cannot be cancelled. It may have already been shipped or delivered.");
        }
    }

    private OrderStatus getOrderStatus(String orderId) {
        // In real system, fetch from database
        // For now, returning a mock status
        return OrderStatus.PROCESSING;
    }

    private Double calculateRefundAmount(String orderId) {
        // In real system, fetch order total from database
        // For now, returning a mock amount
        return 99.99;
    }
}

