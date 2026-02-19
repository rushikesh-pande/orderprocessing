package com.order.processing.controller;

import com.order.processing.dto.CancellationRequest;
import com.order.processing.dto.CancellationResponse;
import com.order.processing.service.OrderCancellationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderCancellationController {

    private final OrderCancellationService cancellationService;

    @PostMapping("/cancel")
    public ResponseEntity<CancellationResponse> cancelOrder(@Valid @RequestBody CancellationRequest request) {
        CancellationResponse response = cancellationService.cancelOrder(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/can-cancel")
    public ResponseEntity<Boolean> canCancelOrder(@PathVariable String orderId) {
        boolean canCancel = cancellationService.canCancelOrder(orderId);
        return ResponseEntity.ok(canCancel);
    }
}

