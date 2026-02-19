package com.order.processing.model;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAYMENT_CONFIRMED,
    PROCESSING,
    READY_TO_SHIP,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    RETURN_REQUESTED,
    RETURNED,
    REFUNDED,
    FAILED
}

