package com.order.processing.service;

import com.order.processing.dto.ProcessOrderRequest;
import com.order.processing.dto.ProcessingResponse;
import com.order.processing.entity.OrderProcessing;
import com.order.processing.entity.ProcessingStatus;
import com.order.processing.repository.OrderProcessingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingService {

    private final OrderProcessingRepository processingRepository;

    @Transactional
    public ProcessingResponse processOrder(ProcessOrderRequest request) {
        log.info("Processing order: {}", request.getOrderId());

        // Check if already processed
        if (processingRepository.existsByOrderId(request.getOrderId())) {
            throw new RuntimeException("Order already processed: " + request.getOrderId());
        }

        // Create processing record
        OrderProcessing processing = OrderProcessing.builder()
                .orderId(request.getOrderId())
                .status(ProcessingStatus.IN_PROGRESS)
                .processingNotes(request.getProcessingNotes())
                .processedBy("SYSTEM")
                .build();

        // Step 1: Check inventory
        log.info("Checking inventory for order: {}", request.getOrderId());
        boolean inventoryAvailable = checkInventory(request.getOrderId());
        processing.setInventoryAvailable(inventoryAvailable);
        processing.setInventoryCheck(inventoryAvailable ? 
                "Inventory available for all items" : 
                "Some items are out of stock");
        processing.setStatus(ProcessingStatus.INVENTORY_CHECKED);

        if (!inventoryAvailable) {
            processing.setStatus(ProcessingStatus.FAILED);
            processing.setProcessedAt(LocalDateTime.now());
            OrderProcessing saved = processingRepository.save(processing);
            log.warn("Order processing failed due to inventory: {}", request.getOrderId());
            return mapToResponse(saved, "Order processing failed: Inventory not available");
        }

        // Step 2: Validate order
        log.info("Validating order: {}", request.getOrderId());
        boolean validationPassed = validateOrder(request.getOrderId());
        processing.setValidationPassed(validationPassed);
        processing.setValidationResult(validationPassed ? 
                "Order validation passed" : 
                "Order validation failed");
        processing.setStatus(ProcessingStatus.VALIDATED);

        if (!validationPassed) {
            processing.setStatus(ProcessingStatus.FAILED);
            processing.setProcessedAt(LocalDateTime.now());
            OrderProcessing saved = processingRepository.save(processing);
            log.warn("Order processing failed due to validation: {}", request.getOrderId());
            return mapToResponse(saved, "Order processing failed: Validation failed");
        }

        // Complete processing
        processing.setStatus(ProcessingStatus.COMPLETED);
        processing.setProcessedAt(LocalDateTime.now());
        OrderProcessing saved = processingRepository.save(processing);
        
        log.info("Order processed successfully: {}", request.getOrderId());
        return mapToResponse(saved, "Order processed successfully");
    }

    public ProcessingResponse getProcessingStatus(String orderId) {
        log.info("Fetching processing status for order: {}", orderId);
        
        OrderProcessing processing = processingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Processing record not found for order: " + orderId));

        return mapToResponse(processing, null);
    }

    private boolean checkInventory(String orderId) {
        // Simulate inventory check
        // In real implementation, this would call inventory service
        return true;
    }

    private boolean validateOrder(String orderId) {
        // Simulate order validation
        // In real implementation, this would validate business rules
        return true;
    }

    private ProcessingResponse mapToResponse(OrderProcessing processing, String message) {
        return ProcessingResponse.builder()
                .orderId(processing.getOrderId())
                .status(processing.getStatus().name())
                .inventoryAvailable(processing.getInventoryAvailable())
                .inventoryCheck(processing.getInventoryCheck())
                .validationPassed(processing.getValidationPassed())
                .validationResult(processing.getValidationResult())
                .processingNotes(processing.getProcessingNotes())
                .processedAt(processing.getProcessedAt())
                .message(message)
                .build();
    }
}

