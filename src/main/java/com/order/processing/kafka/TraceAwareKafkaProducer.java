package com.order.processing.kafka;

import com.orderprocessing.trace.KafkaTraceHeaders;
import com.orderprocessing.trace.TraceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Trace-aware Kafka producer for orderprocessing service.
 * Automatically injects current TraceContext into every Kafka message header
 * so downstream consumers (paymentprocessing, ordertracking) can correlate events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TraceAwareKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Send a Kafka message with the current TraceContext injected into headers.
     *
     * @param topic   Kafka topic
     * @param key     Message key
     * @param payload JSON/string payload
     */
    public void sendWithTrace(String topic, String key, String payload) {
        var ctx = TraceContextHolder.get();
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);

        if (ctx != null) {
            KafkaTraceHeaders.inject(ctx, record.headers());
            log.info("[{}] Sending Kafka message to topic={} key={}", ctx.getTraceId(), topic, key);
        } else {
            log.warn("[TRACE-MISSING] Sending Kafka message without trace context to topic={}", topic);
        }

        kafkaTemplate.send(record);
    }
}
