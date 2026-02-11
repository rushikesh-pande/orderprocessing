package com.order.processing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponse {

    private String offerId;
    private String offerTitle;
    private String description;
    private Integer discountPercentage;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscount;
    private LocalDateTime validUntil;
    private String category;
    private boolean active;
}

