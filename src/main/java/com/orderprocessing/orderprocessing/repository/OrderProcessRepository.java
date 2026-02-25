package com.orderprocessing.orderprocessing.repository;

import com.orderprocessing.orderprocessing.model.OrderProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderProcessRepository extends JpaRepository<OrderProcess, Long> {
    List<OrderProcess> findByStatus(String status);
}
