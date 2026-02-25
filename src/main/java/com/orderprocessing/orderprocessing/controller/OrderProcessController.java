package com.orderprocessing.orderprocessing.controller;

import com.orderprocessing.orderprocessing.model.OrderProcess;
import com.orderprocessing.orderprocessing.service.OrderProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-processing")
@RequiredArgsConstructor
@Slf4j
public class OrderProcessController {

    private final OrderProcessService orderProcessService;

    @PostMapping
    public ResponseEntity<OrderProcess> create(@RequestBody OrderProcess orderProcess) {
        log.info("POST /api/v1/order-processing");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(orderProcessService.create(orderProcess));
    }

    @GetMapping
    public ResponseEntity<List<OrderProcess>> getAll() {
        return ResponseEntity.ok(orderProcessService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProcess> getById(@PathVariable Long id) {
        return orderProcessService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderProcess> update(@PathVariable Long id,
                                                @RequestBody OrderProcess orderProcess) {
        return ResponseEntity.ok(orderProcessService.update(id, orderProcess));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderProcessService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
