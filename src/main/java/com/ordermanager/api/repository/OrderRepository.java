package com.ordermanager.api.repository;

import com.ordermanager.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // poți adăuga metode custom
    List<Order> findByCustomerId(Long customerId);
}

