package com.order.processing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessOrderRequest {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    private String processingNotes;
}

