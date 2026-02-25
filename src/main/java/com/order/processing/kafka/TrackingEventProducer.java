package com.order.processing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Enhancement #1 - Order Tracking
 * Publishes order.processed event at each processing stage.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishOrderProcessed(String orderId, String stage, String message) {
        String payload = String.format(
            "{\"orderId\":\"%s\",\"stage\":\"%s\",\"message\":\"%s\",\"event\":\"ORDER_PROCESSED\",\"timestamp\":\"%s\"}",
            orderId, stage, message, LocalDateTime.now());
        kafkaTemplate.send("order.processed", orderId, payload);
        log.info("[TRACKING] Published order.processed event orderId={} stage={}", orderId, stage);
    }

    public void publishOrderCancelled(String orderId, String reason) {
        String payload = String.format(
            "{\"orderId\":\"%s\",\"reason\":\"%s\",\"event\":\"ORDER_CANCELLED\",\"timestamp\":\"%s\"}",
            orderId, reason, LocalDateTime.now());
        kafkaTemplate.send("order.cancelled", orderId, payload);
        log.info("[TRACKING] Published order.cancelled event orderId={}", orderId);
    }
}
