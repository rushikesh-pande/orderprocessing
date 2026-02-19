package com.order.processing.dto;

import com.order.processing.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancellationResponse {

    private String orderId;
    private OrderStatus status;
    private String message;
    private LocalDateTime cancelledAt;
    private Double refundAmount;
    private String refundStatus;
}

