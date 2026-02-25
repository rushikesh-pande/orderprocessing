package com.orderprocessing.orderprocessing.service;

import com.orderprocessing.orderprocessing.model.OrderProcess;
import com.orderprocessing.orderprocessing.repository.OrderProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessService {

    private final OrderProcessRepository orderProcessRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "order.processed";

    public OrderProcess create(OrderProcess orderProcess) {
        log.info("Creating OrderProcess: {}", orderProcess);
        OrderProcess saved = orderProcessRepository.save(orderProcess);
        kafkaTemplate.send(TOPIC, "ORDERPROCESS_CREATED", saved.toString());
        log.info("OrderProcess created with id: {}", saved.getId());
        return saved;
    }

    public List<OrderProcess> findAll() {
        return orderProcessRepository.findAll();
    }

    public Optional<OrderProcess> findById(Long id) {
        return orderProcessRepository.findById(id);
    }

    public OrderProcess update(Long id, OrderProcess updated) {
        return orderProcessRepository.findById(id).map(existing -> {
            updated.setId(id);
            OrderProcess saved = orderProcessRepository.save(updated);
            kafkaTemplate.send(TOPIC, "ORDERPROCESS_UPDATED", saved.toString());
            log.info("OrderProcess updated: {}", saved.getId());
            return saved;
        }).orElseThrow(() -> new RuntimeException("OrderProcess not found: " + id));
    }

    public void delete(Long id) {
        orderProcessRepository.deleteById(id);
        kafkaTemplate.send(TOPIC, "ORDERPROCESS_DELETED", id.toString());
        log.info("OrderProcess deleted: {}", id);
    }
}
