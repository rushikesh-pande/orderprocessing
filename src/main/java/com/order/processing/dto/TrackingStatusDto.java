package com.order.processing.dto;

import lombok.*;

/**
 * Enhancement #1 - Order Tracking DTO
 * Used when publishing processing stage events.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TrackingStatusDto {
    private String orderId;
    private String stage;
    private String message;
    private String timestamp;
}
