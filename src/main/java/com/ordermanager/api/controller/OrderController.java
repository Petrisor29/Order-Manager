package com.ordermanager.api.controller;

import com.ordermanager.api.dto.AddItemRequest;
import com.ordermanager.api.dto.ChangeStatusRequest;
import com.ordermanager.api.dto.CreateOrderRequest;
import com.ordermanager.api.model.Order;
import com.ordermanager.api.model.OrderStatus;
import com.ordermanager.api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Creează o comandă nouă pentru un client
    // POST /api/orders
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order created = orderService.create(request.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 2. Adaugă un produs într-o comandă
    // POST /api/orders/{orderId}/items
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItem(@PathVariable Long orderId,
                                         @RequestBody AddItemRequest request) {
        Order updated = orderService.addItem(orderId,
                request.getProductId(),
                request.getQuantity());
        return ResponseEntity.ok(updated);
    }

    // 3. Schimbă statusul comenzii
    // PATCH /api/orders/{orderId}/status
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> changeStatus(@PathVariable Long orderId,
                                              @RequestBody ChangeStatusRequest request) {
        Order updated = orderService.changeStatus(orderId, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    // 4. Actualizează cantitatea unui produs din comandă
    // PATCH /api/orders/{orderId}/items/{productId}?qty=5
    @PatchMapping("/{orderId}/items/{productId}")
    public ResponseEntity<Order> updateItemQty(@PathVariable Long orderId,
                                               @PathVariable Long productId,
                                               @RequestParam("qty") int qty) {
        Order updated = orderService.updateItemQty(orderId, productId, qty);
        return ResponseEntity.ok(updated);
    }

    // 5. Calculează totalul comenzii
    // GET /api/orders/{orderId}/total
    @GetMapping("/{orderId}/total")
    public ResponseEntity<Double> getTotal(@PathVariable Long orderId) {
        double total = orderService.total(orderId);
        return ResponseEntity.ok(total);
    }

    // 6. Ia o comandă după id
    // GET /api/orders/{orderId}
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getById(@PathVariable Long orderId) {
        Order order = orderService.byId(orderId);
        return ResponseEntity.ok(order);
    }

    // 7. Toate comenzile (opțional filtrare după customer/status)
    // GET /api/orders
    // GET /api/orders?customerId=1
    // GET /api/orders?status=NEW
    @GetMapping
    public ResponseEntity<List<Order>> getAll(@RequestParam(required = false) Long customerId,
                                              @RequestParam(required = false) OrderStatus status) {
        if (customerId != null) {
            return ResponseEntity.ok(orderService.byCustomer(customerId));
        }
        if (status != null) {
            return ResponseEntity.ok(orderService.byStatus(status));
        }
        return ResponseEntity.ok(orderService.all());
    }
}
