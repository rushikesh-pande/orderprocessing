package com.order.processing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for processing an order.
 * traceId is propagated from createorder service via X-Trace-Id header.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessOrderRequest {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    private String processingNotes;

    /** TraceId propagated from upstream (createorder) — set automatically by TraceFilter */
    private String traceId;

    /** Optional correlation ID */
    private String correlationId;
}
