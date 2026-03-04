package com.order.processing.controller;

import com.order.processing.dto.ProcessingResponse;
import com.order.processing.dto.ProcessOrderRequest;
import com.order.processing.service.OrderProcessingService;
import com.orderprocessing.trace.TraceContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for order processing.
 * TraceId is automatically injected by TraceFilter from trace-context-lib.
 * Propagates traceId to downstream services (paymentprocessing) via Kafka headers.
 */
@RestController
@RequestMapping("/api/v1/processing")
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingController {

    private final OrderProcessingService processingService;

    @PostMapping
    public ResponseEntity<ProcessingResponse> processOrder(@Valid @RequestBody ProcessOrderRequest request) {
        String traceId = TraceContextHolder.getTraceId();
        log.info("[{}] Received process order request for order: {}", traceId, request.getOrderId());

        request.setTraceId(traceId);

        ProcessingResponse response = processingService.processOrder(request);
        response.setTraceId(traceId);

        log.info("[{}] Order processed successfully: {}", traceId, request.getOrderId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ProcessingResponse> getProcessingStatus(@PathVariable String orderId) {
        String traceId = TraceContextHolder.getTraceId();
        log.info("[{}] Getting processing status for order: {}", traceId, orderId);

        ProcessingResponse response = processingService.getProcessingStatus(orderId);
        response.setTraceId(traceId);

        return ResponseEntity.ok(response);
    }
}
