package com.order.processing.controller;

import com.order.processing.dto.ProcessOrderRequest;
import com.order.processing.dto.ProcessingResponse;
import com.order.processing.service.OrderProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processing")
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingController {

    private final OrderProcessingService processingService;

    @PostMapping
    public ResponseEntity<ProcessingResponse> processOrder(@Valid @RequestBody ProcessOrderRequest request) {
        log.info("Received process order request for order: {}", request.getOrderId());
        
        ProcessingResponse response = processingService.processOrder(request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ProcessingResponse> getProcessingStatus(@PathVariable String orderId) {
        log.info("Received get processing status request for order: {}", orderId);
        
        ProcessingResponse response = processingService.getProcessingStatus(orderId);
        
        return ResponseEntity.ok(response);
    }
}

