package com.order.processing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingResponse {

    private String orderId;
    private String status;
    private Boolean inventoryAvailable;
    private String inventoryCheck;
    private Boolean validationPassed;
    private String validationResult;
    private String processingNotes;
    private LocalDateTime processedAt;
    private String message;
}

