package com.order.processing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCancellationEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String ORDER_CANCELLED_TOPIC = "order.cancelled";

    public void publishOrderCancelled(String orderId, String customerId, String reason, Double refundAmount) {
        try {
            ObjectNode event = objectMapper.createObjectNode();
            event.put("orderId", orderId);
            event.put("customerId", customerId);
            event.put("reason", reason);
            event.put("refundAmount", refundAmount);
            event.put("cancelledAt", System.currentTimeMillis());
            event.put("status", "CANCELLED");

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ORDER_CANCELLED_TOPIC, orderId, message);

            log.info("Published order cancelled event for order: {}", orderId);
        } catch (Exception e) {
            log.error("Error publishing order cancelled event: {}", e.getMessage(), e);
        }
    }
}

